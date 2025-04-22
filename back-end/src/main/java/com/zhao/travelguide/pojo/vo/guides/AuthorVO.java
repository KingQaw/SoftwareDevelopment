package com.zhao.travelguide.pojo.vo.guides;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorVO {
    private Long id;
    private String nickname;
    private byte[] avatar;
}
