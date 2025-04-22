package com.zhao.travelguide.server.exception;

public class ActionTwiceException extends RuntimeException {
    public ActionTwiceException(String message) {
        super(message);
    }
}
