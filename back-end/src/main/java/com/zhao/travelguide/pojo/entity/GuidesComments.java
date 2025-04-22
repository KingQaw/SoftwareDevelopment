package com.zhao.travelguide.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuidesComments {
    private String content;
    private String createTime;
    private Long guidesId;
    private Long id;
    private Long usersId;
}
