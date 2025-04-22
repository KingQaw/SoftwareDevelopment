package com.zhao.travelguide.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private Long id;
    private String account;
    private byte[] avatar;
    private String createTime;
    private String nickname;
    private String password;
    private Long rolesId;
    private Long usersStatusId;
}
