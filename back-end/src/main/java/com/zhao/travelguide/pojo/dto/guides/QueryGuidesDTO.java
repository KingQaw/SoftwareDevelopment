package com.zhao.travelguide.pojo.dto.guides;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryGuidesDTO {
    private String title;
    private String orders;
    private String startAt;
    private String endAt;
    private Integer page;
    private Integer size;
    private String auditStatus;
}
