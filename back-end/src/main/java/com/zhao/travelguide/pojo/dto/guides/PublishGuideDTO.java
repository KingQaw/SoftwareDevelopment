package com.zhao.travelguide.pojo.dto.guides;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishGuideDTO {
    private String title;
    private byte[] cover;
    private String content;
}
