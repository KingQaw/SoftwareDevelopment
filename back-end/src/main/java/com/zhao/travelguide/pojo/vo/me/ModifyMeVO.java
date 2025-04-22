package com.zhao.travelguide.pojo.vo.me;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyMeVO {
    private String nickname;
    private byte[] avatar;
}
