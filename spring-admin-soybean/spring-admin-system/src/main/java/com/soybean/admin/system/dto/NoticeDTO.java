package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知公告DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeDTO extends QueryDTO {

    /** 公告ID */
    private Long noticeId;

    /** 公告标题 */
    private String noticeTitle;

    /** 公告类型（1通知 2公告） */
    private String noticeType;

    /** 公告内容 */
    private String noticeContent;

    /** 状态（0正常 1关闭） */
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;
}
