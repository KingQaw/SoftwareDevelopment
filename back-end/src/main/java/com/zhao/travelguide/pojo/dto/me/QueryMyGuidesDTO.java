package com.zhao.travelguide.pojo.dto.me;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryMyGuidesDTO {
    private Integer page;
    private Integer size;
    private String status;
}
