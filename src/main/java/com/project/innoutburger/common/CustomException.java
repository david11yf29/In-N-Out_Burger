package com.project.innoutburger.common;

/*
* 自定義業務異常類
* */
public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
