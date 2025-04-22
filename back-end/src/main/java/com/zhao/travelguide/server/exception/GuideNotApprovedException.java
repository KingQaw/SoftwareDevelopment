package com.zhao.travelguide.server.exception;

public class GuideNotApprovedException extends RuntimeException {
    public GuideNotApprovedException(String id) {
        super("攻略审核中：" + id);
    }
}
