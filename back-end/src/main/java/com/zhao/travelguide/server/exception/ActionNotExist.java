package com.zhao.travelguide.server.exception;

public class ActionNotExist extends RuntimeException {
    public ActionNotExist(String message) {
        super(message);
    }
}
