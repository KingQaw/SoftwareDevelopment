package com.zhao.travelguide.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhao.travelguide.pojo.dto.guides.CommentGuideDTO;
import com.zhao.travelguide.pojo.dto.guides.QueryGuideCommentsDTO;
import com.zhao.travelguide.pojo.dto.guides.QueryGuidesDTO;
import com.zhao.travelguide.pojo.entity.VGuideDetails;
import com.zhao.travelguide.pojo.entity.VGuidesCommentsDetails;
import com.zhao.travelguide.pojo.vo.Result;
import com.zhao.travelguide.pojo.dto.guides.PublishGuideDTO;
import com.zhao.travelguide.pojo.vo.guides.GetGuideByIdVO;
import com.zhao.travelguide.server.exception.*;
import com.zhao.travelguide.server.service.GuidesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guides")
public class GuidesController {
    @Autowired
    private GuidesService guidesService;

    @PostMapping("")
    public Result<?> publishGuide(@RequestHeader("Authorization") String token, @RequestBody PublishGuideDTO publishGuideDTO) {
        guidesService.publishGuide(token, publishGuideDTO);
        return Result.success("发布成功", null);
    }

    @GetMapping("/{id}")
    public Result<GetGuideByIdVO> getGuideById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            GetGuideByIdVO getGuideByIdVO = guidesService.getGuideById(token, id);
            return Result.success("获取成功", getGuideByIdVO);
        } catch (GuideNotApprovedException e){
            return Result.response(1, e.getMessage(), null);
        } catch (GuideNotExistException e) {
            return Result.response(2, e.getMessage(), null);
        }
    }

    @PostMapping("/{id}/comment")
    public Result<?> commentGuide(@PathVariable Long id, @RequestHeader("Authorization") String token, @RequestBody CommentGuideDTO commentGuideDTO) {
        try {
            guidesService.commentGuide(id, token, commentGuideDTO);
            return Result.success("评论成功", null);
        } catch (AccountIsMutedException e1) {
            return Result.response(1, e1.getMessage(), null);
        } catch (GuideNotExistException e3) {
            return Result.response(3, e3.getMessage(), null);
        }
    }

    @DeleteMapping("/comment/{commentId}")
    public Result<?> deleteComment(@PathVariable Long commentId, @RequestHeader("Authorization") String token) {
        try {
            guidesService.deleteComment(commentId, token);
            return Result.success("删除成功", null);
        } catch (GuideCommentNotExistException e) {
            return Result.response(1, e.getMessage(), null);
        }
    }

    @PostMapping("{id}/like")
    public Result<?> likeGuide(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            guidesService.likeGuide(id, token);
            return Result.success("点赞成功", null);
        } catch (GuideNotExistException e1) {
            return Result.response(1, e1.getMessage(), null);
        } catch (ActionTwiceException e2) {
            return Result.response(2, e2.getMessage(), null);
        } catch (ActionForMeException e3) {
            return Result.response(3, e3.getMessage(), null);
        }
    }

    @DeleteMapping("/{id}/like")
    public Result<?> deleteLikeGuide(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            guidesService.deleteLikeGuide(id, token);
            return Result.success("取消点赞成功", null);
        } catch (GuideNotExistException e1) {
            return Result.response(1, e1.getMessage(), null);
        } catch (ActionNotExist e2) {
            return Result.response(2, e2.getMessage(), null);
        }
    }

    @PostMapping("/{id}/favorite")
    public Result<?> favoriteGuide(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            guidesService.favoriteGuide(id, token);
            return Result.success("收藏成功", null);
        } catch (GuideNotExistException e1) {
            return Result.response(1, e1.getMessage(), null);
        } catch (ActionTwiceException e2) {
            return Result.response(2, e2.getMessage(), null);
        } catch (ActionForMeException e3) {
            return Result.response(3, e3.getMessage(), null);
        }
    }

    @DeleteMapping("/{id}/favorite")
    public Result<?> deleteFavoriteGuide(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            guidesService.deleteFavoriteGuide(id, token);
            return Result.success("取消收藏成功", null);
        } catch (GuideNotExistException e1) {
            return Result.response(1, e1.getMessage(), null);
        } catch (ActionNotExist e2) {
            return Result.response(2, e2.getMessage(), null);
        }
    }

    @GetMapping("")
    public Result<?> getGuides(@RequestParam(required = false) String title,
                               @RequestParam(required = false) String orders,
                               @RequestParam(required = false, defaultValue = "1") Integer page,
                               @RequestParam(required = false, defaultValue = "10") Integer size,
                               @RequestParam(required = false) String startAt,
                               @RequestParam(required = false) String endAt,
                               @RequestParam(required = false, defaultValue = "") String auditStatus) {
        QueryGuidesDTO queryGuidesDTO = new QueryGuidesDTO(title, orders, startAt, endAt, page, size, auditStatus);
        Page<VGuideDetails> vGuideDetailsPage = guidesService.getGuides(queryGuidesDTO);
        return Result.success("获取成功", vGuideDetailsPage);
    }

    @GetMapping("/{id}/comments")
    public Result<?> getGuideComments(@PathVariable Long id,
                                      @RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        QueryGuideCommentsDTO queryGuideCommentsDTO = new QueryGuideCommentsDTO(id, page, size);
        Page<VGuidesCommentsDetails> vGuidesCommentsDetailsPage = guidesService.getGuideComments(queryGuideCommentsDTO);
        return Result.success("获取成功", vGuidesCommentsDetailsPage);
    }
}
