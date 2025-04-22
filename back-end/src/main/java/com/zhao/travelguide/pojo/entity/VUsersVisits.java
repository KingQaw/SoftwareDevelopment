package com.zhao.travelguide.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VUsersVisits {
    private Long userId;
    private String account;
    private String nickname;
    private byte[] avatar;
    private String loginTime;
    private String Ip;
    private String region;
    private String status;
}
