package com.zhao.travelguide.pojo.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetVisitsVO {
    private Long id;
    private String createTime;
    private Long usersId;
    private String ip;
    private String status;
    private String region;
}
