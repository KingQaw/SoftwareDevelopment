package com.zhao.travelguide.server.exception;

public class GuideCommentNotExistException extends RuntimeException {
    public GuideCommentNotExistException(String id) {
        super("攻略评论不存在：" + id);
    }
}
