package com.zhao.travelguide.pojo.dto.me;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyMeDTO {
    private String newNickname;
    private byte[] newAvatar;
}
