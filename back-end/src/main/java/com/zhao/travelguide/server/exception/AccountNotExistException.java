package com.zhao.travelguide.server.exception;

public class AccountNotExistException extends RuntimeException {
    public AccountNotExistException(String id) {
        super("账号不存在：" + id);
    }
}
