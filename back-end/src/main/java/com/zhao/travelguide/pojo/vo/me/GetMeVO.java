package com.zhao.travelguide.pojo.vo.me;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMeVO {
    private Long id;
    private String account;
    private String nickname;
    private byte[] avatar;
    private String role;
    private String status;private Integer likesReceivedCount;
    private Integer favoritesReceivedCount;

}
