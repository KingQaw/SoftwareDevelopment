package com.zhao.travelguide.pojo.vo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {
    private Long id;
    private String account;
    private String nickname;
    private byte[] avatar;
    private String role;
    private String token;
}
