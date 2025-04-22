package com.zhao.travelguide.pojo.vo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterVO {
    private Long id;
    private String account;
    private String nickname;
    private String role;
}
