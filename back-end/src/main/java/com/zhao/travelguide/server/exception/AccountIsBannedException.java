package com.zhao.travelguide.server.exception;

public class AccountIsBannedException extends RuntimeException {
    public AccountIsBannedException(String account) {
        super("账号已被封禁：" + account);
    }
}
