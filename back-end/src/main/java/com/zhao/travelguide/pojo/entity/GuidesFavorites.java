package com.zhao.travelguide.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuidesFavorites {
    private Long id;
    private String createTime;
    private Long guidesId;
    private Long usersId;
}
