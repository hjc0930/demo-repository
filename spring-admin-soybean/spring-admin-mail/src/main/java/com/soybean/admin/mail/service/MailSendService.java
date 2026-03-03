package com.soybean.admin.mail.service;

import com.soybean.admin.data.entity.SysMailAccount;
import com.soybean.admin.data.entity.SysMailTemplate;
import com.soybean.admin.mail.dto.MailSendDTO;

import java.util.List;
import java.util.Map;

/**
 * 邮件发送服务接口
 */
public interface MailSendService {

    /**
     * 发送简单邮件
     */
    void sendSimpleMail(Long accountId, String toAddress, String subject, String content);

    /**
     * 发送HTML邮件
     */
    void sendHtmlMail(Long accountId, String toAddress, String subject, String htmlContent);

    /**
     * 使用模板发送邮件
     */
    void sendTemplateMail(Long accountId, Long templateId, String toAddress, Map<String, Object> variables);

    /**
     * 批量发送邮件
     */
    void sendBatchMail(Long accountId, List<String> toAddresses, String subject, String content);

    /**
     * 发送带附件的邮件
     */
    void sendMailWithAttachment(Long accountId, String toAddress, String subject, String content, List<String> filePaths);

    /**
     * 异步发送邮件
     */
    void sendAsyncMail(MailSendDTO mailSendDTO);
}
