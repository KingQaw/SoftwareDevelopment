package com.zhao.travelguide.pojo.dto.me;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyMyPasswordDTO {
    private String oldPassword;
    private String newPassword;
}
