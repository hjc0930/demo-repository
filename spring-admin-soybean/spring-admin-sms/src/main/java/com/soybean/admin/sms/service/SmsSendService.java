package com.soybean.admin.sms.service;

import com.soybean.admin.sms.dto.SmsSendDTO;

import java.util.Map;

/**
 * 短信发送服务接口
 */
public interface SmsSendService {

    /**
     * 发送短信
     */
    void sendSms(Long channelId, String phone, String content);

    /**
     * 使用模板发送短信
     */
    void sendTemplateSms(Long channelId, Long templateId, String phone, Map<String, String> params);

    /**
     * 发送验证码
     */
    void sendVerifyCode(String phone, String code);

    /**
     * 验证验证码
     */
    boolean verifyVerifyCode(String phone, String code);

    /**
     * 批量发送短信
     */
    void sendBatchSms(Long channelId, String[] phones, String content);

    /**
     * 异步发送短信
     */
    void sendAsyncSms(SmsSendDTO smsSendDTO);
}
