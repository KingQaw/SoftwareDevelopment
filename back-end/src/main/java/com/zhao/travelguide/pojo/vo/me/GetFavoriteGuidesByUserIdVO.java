package com.zhao.travelguide.pojo.vo.me;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetFavoriteGuidesByUserIdVO {
    private Long guideId; // 攻略ID
    private String title; // 攻略标题
    private byte[] cover;
    private String content; // 攻略内容
    private String createTime; // 攻略创建时间
    private Long authorId; // 作者ID
    private String authorNickname; // 作者昵称
    private byte[] authorAvatar; // 作者头像
    private String auditStatus; // 审核状态
    private Integer likesCount; // 点赞数
    private Integer favoritesCount; // 收藏数
    private Integer commentsCount; // 评论数
}
