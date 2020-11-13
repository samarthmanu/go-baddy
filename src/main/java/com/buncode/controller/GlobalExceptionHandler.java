package com.buncode.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleException(Exception e){
        return "<input type=\"button\" value=\"Back\" onclick=\"location.href = document.referrer; return false;\"/>\n" +
                "<input type=\"button\" onclick=\"location.href='/'\" value=\"Back to Main\"/>" +
                "<h2>" + e + "</h>";
    }

}
