package com.zhao.travelguide.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhao.travelguide.pojo.dto.guides.CommentGuideDTO;
import com.zhao.travelguide.pojo.dto.guides.PublishGuideDTO;
import com.zhao.travelguide.pojo.dto.guides.QueryGuideCommentsDTO;
import com.zhao.travelguide.pojo.dto.guides.QueryGuidesDTO;
import com.zhao.travelguide.pojo.entity.*;
import com.zhao.travelguide.pojo.vo.guides.AuthorVO;
import com.zhao.travelguide.pojo.vo.guides.GetGuideByIdVO;
import com.zhao.travelguide.server.exception.*;
import com.zhao.travelguide.server.mapper.*;
import com.zhao.travelguide.server.utils.DateUtil;
import com.zhao.travelguide.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GuidesService {
    @Autowired
    private GuidesMapper guidesMapper;
    @Autowired
    private VGuideDetailsMapper vGuideDetailsMapper;
    @Autowired
    private GuidesLikesMapper guidesLikesMapper;
    @Autowired
    private GuidesFavoritesMapper guidesFavoritesMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private GuidesCommentsMapper guidesCommentsMapper;
    @Autowired
    private VGuidesCommentsDetailsMapper vGuidesCommentsDetailsMapper;

    public void publishGuide(String token, PublishGuideDTO publishGuideDTO) {
        Long userId = JwtUtil.getUserIdFromToken(token);
        String title = publishGuideDTO.getTitle();
        byte[] cover = publishGuideDTO.getCover();
        String content = publishGuideDTO.getContent();
        Guides guides = new Guides();
        guides.setUsersId(userId);
        guides.setTitle(title);
        guides.setContent(content);
        guides.setCover(cover);
        guidesMapper.insert(guides);
    }

    public GetGuideByIdVO getGuideById(String token, Long id) throws GuideNotExistException, GuideNotApprovedException {
        Long userId = JwtUtil.getUserIdFromToken(token);
        Guides guide = guidesMapper.selectById(id);
        if (guide == null) {
            throw new GuideNotExistException(id.toString());
        }
//        if (guide.getGuidesAuditStatusId() != 1) {
//            throw new GuideNotApprovedException(id.toString());
//        }
        VGuideDetails vGuideDetails = vGuideDetailsMapper.selectById(id);
        QueryWrapper<GuidesLikes> guidesLikesQueryWrapper = new QueryWrapper<>();
        guidesLikesQueryWrapper.eq("guides_id", id).eq("users_id", userId);
        boolean isLiked = guidesLikesMapper.selectOne(guidesLikesQueryWrapper) != null;
        QueryWrapper<GuidesFavorites> guidesFavoritesQueryWrapper = new QueryWrapper<>();
        guidesFavoritesQueryWrapper.eq("guides_id", id).eq("users_id", userId);
        boolean isFavorited = guidesFavoritesMapper.selectOne(guidesFavoritesQueryWrapper) != null;
        return GetGuideByIdVO.builder()
                .id(vGuideDetails.getId())
                .title(vGuideDetails.getTitle())
                .cover(vGuideDetails.getCover())
                .content(vGuideDetails.getContent())
                .createTime(vGuideDetails.getCreateTime())
                .likeCount(vGuideDetails.getLikesCount())
                .favoriteCount(vGuideDetails.getFavoritesCount())
                .commentCount(vGuideDetails.getCommentsCount())
                .isLiked(isLiked)
                .isFavorited(isFavorited)
                .author(AuthorVO.builder()
                        .id(vGuideDetails.getUserId())
                        .nickname(vGuideDetails.getNickname())
                        .avatar(vGuideDetails.getAvatar())
                        .build())
                .build();

    }

    public void commentGuide(Long id, String token, CommentGuideDTO commentGuideDTO) throws GuideNotExistException, AccountNotExistException, AccountIsMutedException {
        Long userId = JwtUtil.getUserIdFromToken(token);
        String content = commentGuideDTO.getContent();
        Guides guide = guidesMapper.selectById(id);
        if (guide == null) {
            throw new GuideNotExistException(id.toString());
        }
        Users user = usersMapper.selectById(userId);
        if (user == null) {
            throw new AccountNotExistException(userId.toString());
        }
        if (user.getUsersStatusId() == 2 || user.getUsersStatusId() == 3) {
            throw new AccountIsMutedException(userId.toString());
        }
        GuidesComments guidesComments = new GuidesComments();
        guidesComments.setUsersId(userId);
        guidesComments.setGuidesId(id);
        guidesComments.setContent(content);
        guidesCommentsMapper.insert(guidesComments);
    }

    public void deleteComment(Long id, String token) throws GuideCommentNotExistException {
        Long userId = JwtUtil.getUserIdFromToken(token);
        GuidesComments guidesComments = guidesCommentsMapper.selectById(id);
        if (guidesComments == null) {
            throw new GuideCommentNotExistException(id.toString());
        }
        guidesCommentsMapper.deleteById(id);
        return;
    }

    public void likeGuide(Long id, String token) throws GuideNotExistException, ActionTwiceException {
        Long userId = JwtUtil.getUserIdFromToken(token);
        Guides guide = guidesMapper.selectById(id);
        if (guide == null) {
            throw new GuideNotExistException(id.toString());
        }
        QueryWrapper<GuidesLikes> guidesLikesQueryWrapper = new QueryWrapper<>();
        guidesLikesQueryWrapper.eq("guides_id", id).eq("users_id", userId);
        GuidesLikes guidesLikes = guidesLikesMapper.selectOne(guidesLikesQueryWrapper);
        if (guidesLikes != null) {
            throw new ActionTwiceException("点赞过");
        }
        if (Objects.equals(guide.getUsersId(), userId)) {
            throw new ActionForMeException("点赞自己");
        }
        GuidesLikes guidesLikes1 = new GuidesLikes();
        guidesLikes1.setGuidesId(id);
        guidesLikes1.setUsersId(userId);
        guidesLikesMapper.insert(guidesLikes1);
    }

    public void deleteLikeGuide(Long id, String token) throws GuideNotExistException, ActionNotExist {
        Long userId = JwtUtil.getUserIdFromToken(token);
        Guides guide = guidesMapper.selectById(id);
        if (guide == null) {
            throw new GuideNotExistException(id.toString());
        }
        QueryWrapper<GuidesLikes> guidesLikesQueryWrapper = new QueryWrapper<>();
        guidesLikesQueryWrapper.eq("guides_id", id).eq("users_id", userId);
        GuidesLikes guidesLikes = guidesLikesMapper.selectOne(guidesLikesQueryWrapper);
        if (guidesLikes == null) {
            throw new ActionNotExist("未点赞");
        }
        guidesLikesMapper.deleteById(guidesLikes.getId());
    }

    public void favoriteGuide(Long id, String token) throws GuideNotExistException, ActionTwiceException, ActionForMeException {
        Long userId = JwtUtil.getUserIdFromToken(token);
        Guides guide = guidesMapper.selectById(id);
        if (guide == null) {
            throw new GuideNotExistException(id.toString());
        }
        QueryWrapper<GuidesFavorites> guidesFavoritesQueryWrapper = new QueryWrapper<>();
        guidesFavoritesQueryWrapper.eq("guides_id", id).eq("users_id", userId);
        GuidesFavorites guidesFavorites = guidesFavoritesMapper.selectOne(guidesFavoritesQueryWrapper);
        if (guidesFavorites != null) {
            throw new ActionTwiceException("已收藏");
        }
        if (Objects.equals(guide.getUsersId(), userId)) {
            throw new ActionForMeException("收藏自己");
        }
        GuidesFavorites guidesFavorites1 = new GuidesFavorites();
        guidesFavorites1.setGuidesId(id);
        guidesFavorites1.setUsersId(userId);
        guidesFavoritesMapper.insert(guidesFavorites1);
    }

    public void deleteFavoriteGuide(Long id, String token) throws GuideNotExistException {
        Long userId = JwtUtil.getUserIdFromToken(token);
        Guides guides = guidesMapper.selectById(id);
        if (guides == null) {
            throw new GuideNotExistException(id.toString());
        }
        QueryWrapper<GuidesFavorites> guidesFavoritesQueryWrapper = new QueryWrapper<>();
        guidesFavoritesQueryWrapper.eq("guides_id", id).eq("users_id", userId);
        GuidesFavorites guidesFavorites = guidesFavoritesMapper.selectOne(guidesFavoritesQueryWrapper);
        if (guidesFavorites == null) {
            throw new ActionNotExist("未收藏");
        }
        guidesFavoritesMapper.deleteById(guidesFavorites.getId());
    }


    public Page<VGuideDetails> getGuides(QueryGuidesDTO queryGuidesDTO) {
        if (queryGuidesDTO == null) {
            return null;
        }
        Page<VGuideDetails> page = new Page<>(queryGuidesDTO.getPage(), queryGuidesDTO.getSize());
        QueryWrapper<VGuideDetails> vGuideDetailsQueryWrapper = new QueryWrapper<>();
        if (queryGuidesDTO.getTitle() != null && !queryGuidesDTO.getTitle().isEmpty()) {
            vGuideDetailsQueryWrapper.like("title", queryGuidesDTO.getTitle());
        }
        if (queryGuidesDTO.getStartAt() != null && !queryGuidesDTO.getStartAt().isEmpty()) {
            vGuideDetailsQueryWrapper.ge("create_time", DateUtil.string2Date(queryGuidesDTO.getStartAt(), "yyyy-MM-dd"));
        }
        if (queryGuidesDTO.getEndAt() != null && !queryGuidesDTO.getEndAt().isEmpty()) {
            vGuideDetailsQueryWrapper.le("create_time", DateUtil.string2Date(queryGuidesDTO.getEndAt(), "yyyy-MM-dd"));
        }
        if (queryGuidesDTO.getOrders() == null || queryGuidesDTO.getOrders().isEmpty()) {
            vGuideDetailsQueryWrapper.orderByDesc("create_time");
        }
        if (queryGuidesDTO.getAuditStatus() != null && !queryGuidesDTO.getAuditStatus().isEmpty() && !queryGuidesDTO.getAuditStatus().equals("All")) {
            vGuideDetailsQueryWrapper.eq("audit_status", queryGuidesDTO.getAuditStatus());
        }
        // TODO: 添加其他排序逻辑

        return vGuideDetailsMapper.selectPage(page, vGuideDetailsQueryWrapper);
    }

    public Page<VGuidesCommentsDetails> getGuideComments(QueryGuideCommentsDTO queryGuideCommentsDTO) {
        Page<VGuidesCommentsDetails> page = new Page<>(queryGuideCommentsDTO.getPage(), queryGuideCommentsDTO.getSize());
        QueryWrapper<VGuidesCommentsDetails> guidesCommentsQueryWrapper = new QueryWrapper<>();
        guidesCommentsQueryWrapper.eq("guides_id", queryGuideCommentsDTO.getId());
        return vGuidesCommentsDetailsMapper.selectPage(page, guidesCommentsQueryWrapper);
    }
}
