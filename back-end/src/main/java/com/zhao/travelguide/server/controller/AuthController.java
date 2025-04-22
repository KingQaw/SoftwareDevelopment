package com.zhao.travelguide.server.controller;

import com.zhao.travelguide.pojo.dto.auth.LoginDTO;
import com.zhao.travelguide.pojo.dto.auth.RegisterDTO;
import com.zhao.travelguide.pojo.vo.Result;
import com.zhao.travelguide.pojo.vo.auth.LoginVO;
import com.zhao.travelguide.pojo.vo.auth.RegisterVO;
import com.zhao.travelguide.server.exception.AccountIsBannedException;
import com.zhao.travelguide.server.exception.AccountIsExistedException;
import com.zhao.travelguide.server.exception.AccountOrPasswordErrorException;
import com.zhao.travelguide.server.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        try {
            LoginVO loginVO = authService.login(loginDTO, ip);
            return Result.success("登录成功", loginVO);
        } catch (AccountIsBannedException e1) {
            return Result.response(2, e1.getMessage(), null);
        } catch (AccountOrPasswordErrorException e2) {
            return Result.response(1, e2.getMessage(), null);
        }
    }

    @PostMapping("/register")
    public Result<RegisterVO> register(@RequestBody RegisterDTO registerDTO) {
        try {
            RegisterVO registerVO = authService.register(registerDTO);
            return Result.success("注册成功", registerVO);
        } catch (AccountIsExistedException e) {
            return Result.response(1, e.getMessage(), null);
        }
    }
}
