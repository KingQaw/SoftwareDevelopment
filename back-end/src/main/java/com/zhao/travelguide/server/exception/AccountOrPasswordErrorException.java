package com.zhao.travelguide.server.exception;

public class AccountOrPasswordErrorException extends RuntimeException {
    public AccountOrPasswordErrorException(String account, String password) {
        super("账号或密码错误，账号：" + account + "，密码：" + password);
    }
}
