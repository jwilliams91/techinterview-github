package com.ghapi.praeses.controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
	@ExceptionHandler(IOException.class)
    public void handleClientException(IOException e) {
        e.printStackTrace();
    }
}
