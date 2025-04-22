package com.zhao.travelguide.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visits {
    private Long id;
    private String createTime;
    private Long usersId;
    private String ip;
    private String status;
    private String region;
}
