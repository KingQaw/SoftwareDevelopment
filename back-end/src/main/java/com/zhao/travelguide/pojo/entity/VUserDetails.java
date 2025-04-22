package com.zhao.travelguide.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VUserDetails {
    private Long id;
    private String account;
    private String nickname;
    private byte[] avatar;
    private String role;
    private String status;
    private String createTime;
    private Integer favoritesGivenCount;
    private Integer guidesPublishedCount;
    private Integer likesReceivedCount;
    private Integer favoritesReceivedCount;
}
