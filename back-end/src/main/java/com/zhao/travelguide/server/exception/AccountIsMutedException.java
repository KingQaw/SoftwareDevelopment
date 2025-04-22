package com.zhao.travelguide.server.exception;

public class AccountIsMutedException extends RuntimeException {
    public AccountIsMutedException(String id) {
        super("账号被禁言：" + id);
    }
}
