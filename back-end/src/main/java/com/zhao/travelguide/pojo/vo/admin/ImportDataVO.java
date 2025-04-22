package com.zhao.travelguide.pojo.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportDataVO {
    private int total;
    private int error;
    private List<String> errorAccounts;
}
