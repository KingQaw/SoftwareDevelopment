package com.zhao.travelguide.pojo.dto.guides;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryGuideCommentsDTO {
    private Long id;
    private Integer page;
    private Integer size;
}
