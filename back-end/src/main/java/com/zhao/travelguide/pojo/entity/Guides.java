package com.zhao.travelguide.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guides {
    private Long id;
    private String auditTime;
    private String content;
    private byte[] cover;
    private String createTime;
    private Long guidesAuditStatusId;
    private String title;
    private Long usersId;
}
