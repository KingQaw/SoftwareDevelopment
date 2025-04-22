package com.zhao.travelguide.server.exception;

public class AccountIsExistedException extends RuntimeException {
    public AccountIsExistedException(String account) {
        super("账号已存在：" + account);
    }
}
