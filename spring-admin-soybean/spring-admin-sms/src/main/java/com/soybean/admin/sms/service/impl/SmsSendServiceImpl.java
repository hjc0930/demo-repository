package com.soybean.admin.sms.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soybean.admin.data.entity.SysSmsChannel;
import com.soybean.admin.data.entity.SysSmsLog;
import com.soybean.admin.data.entity.SysSmsTemplate;
import com.soybean.admin.data.mapper.SysSmsChannelMapper;
import com.soybean.admin.data.mapper.SysSmsLogMapper;
import com.soybean.admin.data.mapper.SysSmsTemplateMapper;
import com.soybean.admin.security.utils.RedisCache;
import com.soybean.admin.sms.dto.SmsSendDTO;
import com.soybean.admin.sms.service.SmsSendService;
import com.soybean.admin.tenant.context.TenantContext;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 短信发送服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsSendServiceImpl implements SmsSendService {

    private final SysSmsChannelMapper smsChannelMapper;
    private final SysSmsTemplateMapper smsTemplateMapper;
    private final SysSmsLogMapper smsLogMapper;
    private final RedisCache redisCache;

    private static final String VERIFY_CODE_KEY_PREFIX = "sms:verify_code:";
    private static final int VERIFY_CODE_EXPIRE_MINUTES = 5;
    private static final int VERIFY_CODE_LENGTH = 6;

    @Value("${sms.mock.enabled:false}")
    private boolean mockEnabled;

    @Override
    public void sendSms(Long channelId, String phone, String content) {
        SysSmsChannel channel = getSmsChannel(channelId);

        try {
            boolean success;

            if (mockEnabled) {
                // 模拟发送
                success = mockSendSms(channel, phone, content);
            } else {
                // 实际发送
                success = sendRealSms(channel, phone, content);
            }

            if (success) {
                saveSmsLog(channelId, null, phone, content, "1", null);
                log.info("SMS sent successfully: phone={}, channel={}", phone, channel.getChannelName());
            } else {
                saveSmsLog(channelId, null, phone, content, "2", "Send failed");
                throw new RuntimeException("Failed to send SMS");
            }
        } catch (Exception e) {
            saveSmsLog(channelId, null, phone, content, "2", e.getMessage());
            log.error("Failed to send SMS", e);
            throw new RuntimeException("Failed to send SMS: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendTemplateSms(Long channelId, Long templateId, String phone, Map<String, String> params) {
        SysSmsTemplate template = smsTemplateMapper.selectById(templateId);
        if (template == null) {
            throw new RuntimeException("SMS template not found: " + templateId);
        }

        // 替换模板参数
        String content = template.getContent();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                content = content.replace("${" + entry.getKey() + "}", entry.getValue());
            }
        }

        if (channelId == null) {
            channelId = template.getChannelId();
        }

        sendSms(channelId, phone, content);
    }

    @Override
    public void sendVerifyCode(String phone, String code) {
        if (code == null) {
            code = generateVerifyCode();
        }

        // 检查发送频率限制
        String rateLimitKey = "sms:rate_limit:" + phone;
        if (redisCache.getCacheObject(rateLimitKey) != null) {
            throw new RuntimeException("验证码发送过于频繁，请稍后再试");
        }

        // 存储验证码
        String key = VERIFY_CODE_KEY_PREFIX + phone;
        redisCache.setCacheObject(key, code, VERIFY_CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 设置发送频率限制（60秒）
        redisCache.setCacheObject(rateLimitKey, "1", 60, TimeUnit.SECONDS);

        // 发送验证码
        String content = "您的验证码是：" + code + "，" + VERIFY_CODE_EXPIRE_MINUTES + "分钟内有效。";

        // 获取默认短信渠道
        SysSmsChannel defaultChannel = smsChannelMapper.selectOne(
            new LambdaQueryWrapper<SysSmsChannel>()
                .eq(SysSmsChannel::getIsDefault, "1")
                .eq(SysSmsChannel::getStatus, "1")
                .last("LIMIT 1")
        );

        if (defaultChannel != null) {
            sendSms(defaultChannel.getChannelId(), phone, content);
        } else {
            log.warn("No default SMS channel found, using mock send");
            mockSendSms(null, phone, content);
        }

        log.info("Verify code sent: phone={}, code={}", phone, code);
    }

    @Override
    public boolean verifyVerifyCode(String phone, String code) {
        String key = VERIFY_CODE_KEY_PREFIX + phone;
        String cachedCode = redisCache.getCacheObject(key);

        if (cachedCode == null) {
            return false;
        }

        boolean result = cachedCode.equals(code);
        if (result) {
            // 验证成功后删除验证码
            redisCache.deleteObject(key);
        }

        return result;
    }

    @Override
    public void sendBatchSms(Long channelId, String[] phones, String content) {
        for (String phone : phones) {
            try {
                sendSms(channelId, phone, content);
            } catch (Exception e) {
                log.error("Failed to send SMS to: {}", phone, e);
            }
        }
    }

    @Async
    @Override
    public void sendAsyncSms(SmsSendDTO smsSendDTO) {
        try {
            if (smsSendDTO.getTemplateId() != null) {
                sendTemplateSms(
                    smsSendDTO.getChannelId(),
                    smsSendDTO.getTemplateId(),
                    smsSendDTO.getPhone(),
                    smsSendDTO.getParams()
                );
            } else {
                sendSms(
                    smsSendDTO.getChannelId(),
                    smsSendDTO.getPhone(),
                    smsSendDTO.getContent()
                );
            }
        } catch (Exception e) {
            log.error("Failed to send async SMS", e);
        }
    }

    /**
     * 获取短信渠道
     */
    private SysSmsChannel getSmsChannel(Long channelId) {
        SysSmsChannel channel = smsChannelMapper.selectById(channelId);
        if (channel == null) {
            throw new RuntimeException("SMS channel not found: " + channelId);
        }
        if (!"1".equals(channel.getStatus())) {
            throw new RuntimeException("SMS channel is disabled: " + channelId);
        }
        return channel;
    }

    /**
     * 实际发送短信
     */
    private boolean sendRealSms(SysSmsChannel channel, String phone, String content) {
        try {
            String provider = channel.getProvider();

            if ("aliyun".equals(provider)) {
                return sendAliyunSms(channel, phone, content);
            } else if ("tencent".equals(provider)) {
                return sendTencentSms(channel, phone, content);
            } else {
                log.warn("Unsupported SMS provider: {}", provider);
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to send real SMS", e);
            return false;
        }
    }

    /**
     * 发送阿里云短信
     */
    private boolean sendAliyunSms(SysSmsChannel channel, String phone, String content) {
        // TODO: 实现阿里云短信API调用
        // 这里需要使用阿里云SDK
        log.info("Sending Aliyun SMS: phone={}, content={}", phone, content);
        return true;
    }

    /**
     * 发送腾讯云短信
     */
    private boolean sendTencentSms(SysSmsChannel channel, String phone, String content) {
        // TODO: 实现腾讯云短信API调用
        log.info("Sending Tencent SMS: phone={}, content={}", phone, content);
        return true;
    }

    /**
     * 模拟发送短信（用于测试）
     */
    private boolean mockSendSms(SysSmsChannel channel, String phone, String content) {
        log.info("Mock SMS sent: phone={}, content={}", phone, content);
        return true;
    }

    /**
     * 生成验证码
     */
    private String generateVerifyCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < VERIFY_CODE_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 保存短信日志
     */
    private void saveSmsLog(Long channelId, Long templateId, String phone, String content,
                           String status, String errorMsg) {
        SysSmsLog smsLog = new SysSmsLog();
        smsLog.setChannelId(channelId);
        smsLog.setTemplateId(templateId);
        smsLog.setPhone(phone);
        smsLog.setContent(content);
        smsLog.setStatus(status);
        smsLog.setErrorMsg(errorMsg);
        smsLog.setSendTime(LocalDateTime.now());
        smsLog.setTenantId(TenantContext.getTenantId());

        smsLogMapper.insert(smsLog);
    }
}
