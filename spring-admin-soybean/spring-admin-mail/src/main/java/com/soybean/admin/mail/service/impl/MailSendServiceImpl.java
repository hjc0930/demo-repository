package com.soybean.admin.mail.service.impl;

import com.soybean.admin.data.entity.SysMailAccount;
import com.soybean.admin.data.entity.SysMailLog;
import com.soybean.admin.data.entity.SysMailTemplate;
import com.soybean.admin.data.mapper.SysMailAccountMapper;
import com.soybean.admin.data.mapper.SysMailLogMapper;
import com.soybean.admin.data.mapper.SysMailTemplateMapper;
import com.soybean.admin.mail.dto.MailSendDTO;
import com.soybean.admin.mail.service.MailSendService;
import com.soybean.admin.tenant.context.TenantContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 邮件发送服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailSendServiceImpl implements MailSendService {

    private final SysMailAccountMapper mailAccountMapper;
    private final SysMailTemplateMapper mailTemplateMapper;
    private final SysMailLogMapper mailLogMapper;
    private final Configuration freemarkerConfiguration;

    @Override
    public void sendSimpleMail(Long accountId, String toAddress, String subject, String content) {
        SysMailAccount account = getMailAccount(accountId);
        JavaMailSender mailSender = createMailSender(account);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(account.getFromAddress());

        try {
            mailSender.send(message);
            saveMailLog(accountId, null, toAddress, subject, content, "1", null);
            log.info("Simple mail sent successfully: to={}", toAddress);
        } catch (Exception e) {
            saveMailLog(accountId, null, toAddress, subject, content, "2", e.getMessage());
            log.error("Failed to send simple mail", e);
            throw new RuntimeException("Failed to send mail: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendHtmlMail(Long accountId, String toAddress, String subject, String htmlContent) {
        SysMailAccount account = getMailAccount(accountId);
        JavaMailSender mailSender = createMailSender(account);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom(account.getFromAddress());

            mailSender.send(message);
            saveMailLog(accountId, null, toAddress, subject, htmlContent, "1", null);
            log.info("HTML mail sent successfully: to={}", toAddress);
        } catch (Exception e) {
            saveMailLog(accountId, null, toAddress, subject, htmlContent, "2", e.getMessage());
            log.error("Failed to send HTML mail", e);
            throw new RuntimeException("Failed to send mail: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendTemplateMail(Long accountId, Long templateId, String toAddress, Map<String, Object> variables) {
        SysMailTemplate template = mailTemplateMapper.selectById(templateId);
        if (template == null) {
            throw new RuntimeException("Mail template not found: " + templateId);
        }

        try {
            // 使用FreeMarker渲染模板
            Template freemarkerTemplate = freemarkerConfiguration.getTemplate(template.getTemplateCode() + ".ftl");
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, variables);

            sendHtmlMail(accountId, toAddress, template.getSubject(), content);
        } catch (Exception e) {
            log.error("Failed to process mail template", e);
            throw new RuntimeException("Failed to process template: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendBatchMail(Long accountId, List<String> toAddresses, String subject, String content) {
        for (String toAddress : toAddresses) {
            try {
                sendSimpleMail(accountId, toAddress, subject, content);
            } catch (Exception e) {
                log.error("Failed to send mail to: {}", toAddress, e);
            }
        }
    }

    @Override
    public void sendMailWithAttachment(Long accountId, String toAddress, String subject, String content, List<String> filePaths) {
        SysMailAccount account = getMailAccount(accountId);
        JavaMailSender mailSender = createMailSender(account);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom(account.getFromAddress());

            // 添加附件
            if (filePaths != null) {
                for (String filePath : filePaths) {
                    File file = new File(filePath);
                    if (file.exists()) {
                        helper.addAttachment(file.getName(), file);
                    }
                }
            }

            mailSender.send(message);
            saveMailLog(accountId, null, toAddress, subject, content, "1", null);
            log.info("Mail with attachment sent successfully: to={}", toAddress);
        } catch (Exception e) {
            saveMailLog(accountId, null, toAddress, subject, content, "2", e.getMessage());
            log.error("Failed to send mail with attachment", e);
            throw new RuntimeException("Failed to send mail: " + e.getMessage(), e);
        }
    }

    @Async
    @Override
    public void sendAsyncMail(MailSendDTO mailSendDTO) {
        try {
            if (mailSendDTO.getTemplateId() != null) {
                sendTemplateMail(
                    mailSendDTO.getAccountId(),
                    mailSendDTO.getTemplateId(),
                    mailSendDTO.getToAddress(),
                    mailSendDTO.getVariables()
                );
            } else if (Boolean.TRUE.equals(mailSendDTO.getIsHtml())) {
                sendHtmlMail(
                    mailSendDTO.getAccountId(),
                    mailSendDTO.getToAddress(),
                    mailSendDTO.getSubject(),
                    mailSendDTO.getContent()
                );
            } else {
                sendSimpleMail(
                    mailSendDTO.getAccountId(),
                    mailSendDTO.getToAddress(),
                    mailSendDTO.getSubject(),
                    mailSendDTO.getContent()
                );
            }
        } catch (Exception e) {
            log.error("Failed to send async mail", e);
        }
    }

    /**
     * 获取邮件账号
     */
    private SysMailAccount getMailAccount(Long accountId) {
        SysMailAccount account = mailAccountMapper.selectById(accountId);
        if (account == null) {
            throw new RuntimeException("Mail account not found: " + accountId);
        }
        if (!"1".equals(account.getStatus())) {
            throw new RuntimeException("Mail account is disabled: " + accountId);
        }
        return account;
    }

    /**
     * 创建邮件发送器
     */
    private JavaMailSender createMailSender(SysMailAccount account) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(account.getSmtpHost());
        mailSender.setPort(account.getSmtpPort());
        mailSender.setUsername(account.getUsername());
        mailSender.setPassword(account.getPassword());
        mailSender.setProtocol("smtp");
        mailSender.setDefaultEncoding("UTF-8");

        // 配置属性
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", account.getSmtpPort() == 465);
        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

    /**
     * 保存邮件发送日志
     */
    private void saveMailLog(Long accountId, Long templateId, String toAddress, String subject,
                             String content, String status, String errorMsg) {
        SysMailLog mailLog = new SysMailLog();
        mailLog.setAccountId(accountId);
        mailLog.setTemplateId(templateId);
        mailLog.setToAddress(toAddress);
        mailLog.setSubject(subject);
        mailLog.setContent(content);
        mailLog.setStatus(status);
        mailLog.setErrorMsg(errorMsg);
        mailLog.setSendTime(LocalDateTime.now());
        mailLog.setTenantId(TenantContext.getTenantId());

        mailLogMapper.insert(mailLog);
    }
}
