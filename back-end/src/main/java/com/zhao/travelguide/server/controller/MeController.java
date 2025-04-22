package com.zhao.travelguide.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhao.travelguide.pojo.dto.me.ModifyMeDTO;
import com.zhao.travelguide.pojo.dto.me.ModifyMyPasswordDTO;
import com.zhao.travelguide.pojo.dto.me.QueryMyFavoritesDTO;
import com.zhao.travelguide.pojo.dto.me.QueryMyGuidesDTO;
import com.zhao.travelguide.pojo.entity.VGuideDetails;
import com.zhao.travelguide.pojo.vo.Result;
import com.zhao.travelguide.pojo.vo.me.GetFavoriteGuidesByUserIdVO;
import com.zhao.travelguide.pojo.vo.me.GetMeVO;
import com.zhao.travelguide.pojo.vo.me.ModifyMeVO;
import com.zhao.travelguide.server.exception.AccountOrPasswordErrorException;
import com.zhao.travelguide.server.exception.UserNotExistException;
import com.zhao.travelguide.server.service.MeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
public class MeController {
    @Autowired
    private MeService meService;

    @GetMapping("")
    public Result<GetMeVO> getMe(@RequestHeader("Authorization") String token) {
        try {
            GetMeVO getMeVO = meService.getMe(token);
            return Result.success("获取成功", getMeVO);
        } catch (UserNotExistException e) {
            return Result.response(1, e.getMessage(), null);
        }
    }

    @PostMapping("")
    public Result<ModifyMeVO> modifyMe(@RequestHeader("Authorization") String token, @RequestBody ModifyMeDTO modifyMeDTO) {
        ModifyMeVO modifyMeVO = meService.modifyMe(token, modifyMeDTO);
        return Result.success("修改成功", modifyMeVO);
    }

    @PostMapping("/password")
    public Result<?> modifyMyPassword(@RequestHeader("Authorization") String token, @RequestBody ModifyMyPasswordDTO modifyMyPasswordDTO) {
        try {
            meService.modifyMyPassword(token, modifyMyPasswordDTO);
            return Result.success("修改成功", null);
        } catch (AccountOrPasswordErrorException e) {
            return Result.response(1, e.getMessage(), null);
        }
    }

    @GetMapping("/guides")
    public Result<?> getMyGuides(@RequestHeader("Authorization") String token,
                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                 @RequestParam(required = false, defaultValue = "10") Integer size,
                                 @RequestParam(required = false, defaultValue = "ALL") String auditStatus) {
        QueryMyGuidesDTO queryMyGuidesDTO = new QueryMyGuidesDTO(page, size, auditStatus);
        Page<VGuideDetails> vGuideDetailsPage = meService.getMyGuides(token, queryMyGuidesDTO);
        return Result.success("获取成功", vGuideDetailsPage);
    }

    @GetMapping("/favorites")
    public Result<?> getMyFavorites(@RequestHeader("Authorization") String token,
                                   @RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "10") Integer size) {
        QueryMyFavoritesDTO queryMyFavoritesDTO = new QueryMyFavoritesDTO(page, size);
        Page<GetFavoriteGuidesByUserIdVO> vGuideDetailsPage = meService.getMyFavorites(token, queryMyFavoritesDTO);
        return Result.success("获取成功", vGuideDetailsPage);
    }
}
