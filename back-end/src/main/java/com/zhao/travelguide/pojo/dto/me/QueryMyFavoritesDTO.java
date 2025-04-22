package com.zhao.travelguide.pojo.dto.me;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryMyFavoritesDTO {
    private Integer page;
    private Integer size;
}
