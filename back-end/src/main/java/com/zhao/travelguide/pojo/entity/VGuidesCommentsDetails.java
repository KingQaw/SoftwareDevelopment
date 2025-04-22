package com.zhao.travelguide.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VGuidesCommentsDetails {
    private Long id;
    private Long guidesId;
    private Long usersId;
    private String nickname;
    private byte[] avatar;
    private String content;
    private String createTime;
}
