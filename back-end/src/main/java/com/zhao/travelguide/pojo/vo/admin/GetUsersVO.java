package com.zhao.travelguide.pojo.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUsersVO {
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
