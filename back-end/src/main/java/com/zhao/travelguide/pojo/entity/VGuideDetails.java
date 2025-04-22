package com.zhao.travelguide.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VGuideDetails {
    private Long id;
    private String title;
    private byte[] cover;
    private String content;
    private String createTime;
    private String auditTime;
    private String auditStatus;
    private Long userId;
    private String nickname;
    private byte[] avatar;
    private Integer likesCount;
    private Integer favoritesCount;
    private Integer commentsCount;

}
