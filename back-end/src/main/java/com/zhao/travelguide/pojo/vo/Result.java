package com.zhao.travelguide.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result <T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(0, message, data);
    }

    public static <T> Result<T> response(Integer code, String message, T data) {
        return new Result<>(code, message, data);
    }
}
