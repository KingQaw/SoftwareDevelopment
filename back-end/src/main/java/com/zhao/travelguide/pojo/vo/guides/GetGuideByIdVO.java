package com.zhao.travelguide.pojo.vo.guides;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetGuideByIdVO {
    private Long id;
    private String title;
    private byte[] cover;
    private String content;
    private String createTime;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer commentCount;
    private boolean isLiked;
    private boolean isFavorited;
    private AuthorVO author;
}
