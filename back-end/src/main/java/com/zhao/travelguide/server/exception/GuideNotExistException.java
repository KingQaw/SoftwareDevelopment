package com.zhao.travelguide.server.exception;

public class GuideNotExistException extends RuntimeException {
    public GuideNotExistException(String id) {
        super("攻略不存在：" + id);
    }
}
