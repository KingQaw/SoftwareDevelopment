package com.zhao.travelguide.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.travelguide.pojo.dto.auth.LoginDTO;
import com.zhao.travelguide.pojo.dto.auth.RegisterDTO;
import com.zhao.travelguide.pojo.entity.Users;
import com.zhao.travelguide.pojo.entity.Visits;
import com.zhao.travelguide.pojo.vo.auth.LoginVO;
import com.zhao.travelguide.pojo.vo.auth.RegisterVO;
import com.zhao.travelguide.server.exception.AccountIsBannedException;
import com.zhao.travelguide.server.exception.AccountIsExistedException;
import com.zhao.travelguide.server.exception.AccountOrPasswordErrorException;
import com.zhao.travelguide.server.mapper.RolesMapper;
import com.zhao.travelguide.server.mapper.UsersMapper;
import com.zhao.travelguide.server.mapper.VisitsMapper;
import com.zhao.travelguide.server.utils.IpUtil;
import com.zhao.travelguide.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private VisitsMapper visitsMapper;

    public LoginVO login(LoginDTO loginDTO, String ip) throws AccountIsBannedException, AccountOrPasswordErrorException {
        String account = loginDTO.getAccount();
        String password = loginDTO.getPassword();
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("account", account).eq("password", password);
        Users users = usersMapper.selectOne(usersQueryWrapper);
        QueryWrapper<Users> usersQueryWrapper2 = new QueryWrapper<>();
        usersQueryWrapper2.eq("account", account);
        Users users2 = usersMapper.selectOne(usersQueryWrapper2);
        System.out.println(ip);
        String region = IpUtil.getIpRegion(ip);
        if (users == null) {
            if (users2 != null) {
                visitsMapper.insert(new Visits(null, null, users2.getId(), ip, "密码错误", region));
            }
            throw new AccountOrPasswordErrorException(account, password);
        }
        if (users.getUsersStatusId() == 3) {
            visitsMapper.insert(new Visits(null, null, users2.getId(), ip, "账号被封禁", region));
            throw new AccountIsBannedException(account);
        }
        visitsMapper.insert(new Visits(null, null, users.getId(), ip, "登录成功", region));
        return LoginVO.builder()
                .id(users.getId())
                .account(users.getAccount())
                .nickname(users.getNickname())
                .avatar(users.getAvatar())
                .role(rolesMapper.selectById(users.getRolesId()).getName())
                .token(JwtUtil.generateToken(users.getId(), account))
                .build();
    }

    public RegisterVO register(RegisterDTO registerDTO) throws AccountIsExistedException {
        String account = registerDTO.getAccount();
        String password = registerDTO.getPassword();
        String nickname = registerDTO.getNickname();

        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("account", account);
        Long count = usersMapper.selectCount(usersQueryWrapper);
        if (count != 0) {
            throw new AccountIsExistedException(account);
        }
        Users insertUser = new Users();
        insertUser.setAccount(account);
        insertUser.setPassword(password);
        insertUser.setNickname(nickname);
        usersMapper.insert(insertUser);
        Users newUser = usersMapper.selectOne(usersQueryWrapper);
        String role = rolesMapper.selectById(newUser.getRolesId()).getName();
        return RegisterVO.builder()
                .id(newUser.getId())
                .account(newUser.getAccount())
                .nickname(newUser.getNickname())
                .role(role)
                .build();
    }
}
