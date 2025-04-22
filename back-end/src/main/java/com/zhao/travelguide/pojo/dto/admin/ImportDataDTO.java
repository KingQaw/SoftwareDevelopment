package com.zhao.travelguide.pojo.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportDataDTO {
    private String account;
    private String nickname;
    private String password;
}
