package com.zhao.travelguide.pojo.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryUsersDTO {
    private Integer page;
    private Integer size;
    private String status;
}
