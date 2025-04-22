package com.zhao.travelguide.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhao.travelguide.pojo.dto.me.ModifyMeDTO;
import com.zhao.travelguide.pojo.dto.me.ModifyMyPasswordDTO;
import com.zhao.travelguide.pojo.dto.me.QueryMyFavoritesDTO;
import com.zhao.travelguide.pojo.dto.me.QueryMyGuidesDTO;
import com.zhao.travelguide.pojo.entity.Users;
import com.zhao.travelguide.pojo.entity.VGuideDetails;
import com.zhao.travelguide.pojo.entity.VUserDetails;
import com.zhao.travelguide.pojo.vo.me.GetFavoriteGuidesByUserIdVO;
import com.zhao.travelguide.pojo.vo.me.GetMeVO;
import com.zhao.travelguide.pojo.vo.me.ModifyMeVO;
import com.zhao.travelguide.server.exception.AccountOrPasswordErrorException;
import com.zhao.travelguide.server.exception.UserNotExistException;
import com.zhao.travelguide.server.mapper.GuidesMapper;
import com.zhao.travelguide.server.mapper.UsersMapper;
import com.zhao.travelguide.server.mapper.VGuideDetailsMapper;
import com.zhao.travelguide.server.mapper.VUserDetailsMapper;
import com.zhao.travelguide.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;

@Service
public class MeService {
    @Autowired
    private VUserDetailsMapper vUserDetailsMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private VGuideDetailsMapper vGuideDetailsMapper;
    @Autowired
    private GuidesMapper guidesMapper;

    public GetMeVO getMe(String token) throws UserNotExistException {
        Long userId = JwtUtil.getUserIdFromToken(token);
        VUserDetails vUserDetails = vUserDetailsMapper.selectById(userId);
        if (vUserDetails == null) {
            throw new UserNotExistException();
        }
        return GetMeVO.builder()
                .id(vUserDetails.getId())
                .account(vUserDetails.getAccount())
                .nickname(vUserDetails.getNickname())
                .avatar(vUserDetails.getAvatar())
                .role(vUserDetails.getRole())
                .status(vUserDetails.getStatus())
                .likesReceivedCount(vUserDetails.getLikesReceivedCount())
                .favoritesReceivedCount(vUserDetails.getFavoritesReceivedCount())
                .build();
    }

    public ModifyMeVO modifyMe(String token, ModifyMeDTO modifyMeDTO) {
        Long userId = JwtUtil.getUserIdFromToken(token);
        String nickname = modifyMeDTO.getNewNickname();
        byte[] avatar = modifyMeDTO.getNewAvatar();
        if (nickname == null && avatar == null) {
            return null;
        }
        UpdateWrapper<Users> usersUpdateWrapper = new UpdateWrapper<>();
        usersUpdateWrapper.eq("id", userId);
        if (nickname != null) {
            usersUpdateWrapper.set("nickname", nickname);
        }
        if (avatar != null && avatar.length > 0) {
            usersUpdateWrapper.set("avatar", avatar);
        }
        usersMapper.update(null, usersUpdateWrapper);

        Users users = usersMapper.selectById(userId);
        return ModifyMeVO.builder()
                .nickname(users.getNickname())
                .avatar(users.getAvatar())
                .build();
    }

    public void modifyMyPassword(String token, ModifyMyPasswordDTO modifyMyPasswordDTO) throws AccountOrPasswordErrorException {
        Long userId = JwtUtil.getUserIdFromToken(token);
        String oldPassword = modifyMyPasswordDTO.getOldPassword();
        String newPassword = modifyMyPasswordDTO.getNewPassword();
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("id", userId).eq("password", oldPassword);
        long count = usersMapper.selectCount(usersQueryWrapper);
        if (count == 0) {
            throw new AccountOrPasswordErrorException(null, oldPassword);
        }
        UpdateWrapper<Users> usersUpdateWrapper = new UpdateWrapper<>();
        usersUpdateWrapper.eq("id", userId).set("password", newPassword);
        usersMapper.update(null, usersUpdateWrapper);
        return;
    }

    public Page<VGuideDetails> getMyGuides(String token, QueryMyGuidesDTO queryMyGuidesDTO) {
        Long userId = JwtUtil.getUserIdFromToken(token);
        Page<VGuideDetails> page = new Page<>(queryMyGuidesDTO.getPage(), queryMyGuidesDTO.getSize());
        QueryWrapper<VGuideDetails> vGuideDetailsQueryWrapper = new QueryWrapper<>();
        vGuideDetailsQueryWrapper.eq("user_id", userId);
        if (queryMyGuidesDTO.getStatus() != null && !queryMyGuidesDTO.getStatus().isEmpty()) {
            if (!queryMyGuidesDTO.getStatus().equals("ALL")) {
                vGuideDetailsQueryWrapper.eq("audit_status", queryMyGuidesDTO.getStatus());
            }
        }
        return vGuideDetailsMapper.selectPage(page, vGuideDetailsQueryWrapper);
    }

    public Page<GetFavoriteGuidesByUserIdVO> getMyFavorites(String token, QueryMyFavoritesDTO queryMyFavoritesDTO) {
        Long userId = JwtUtil.getUserIdFromToken(token);
        Page<GetFavoriteGuidesByUserIdVO> page = new Page<>(queryMyFavoritesDTO.getPage(), queryMyFavoritesDTO.getSize());
        return guidesMapper.getFavoriteGuidesByUserId(page, userId);
    }
}
