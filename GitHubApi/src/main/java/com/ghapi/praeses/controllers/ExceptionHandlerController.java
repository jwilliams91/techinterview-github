package com.ghapi.praeses.controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
	//Will catch all IOExceptions, this was neccessary as the GitHub Library
	//I used methods almost always had a throws declaration. Just keeps code clean.
	@ExceptionHandler(IOException.class)
    public void handleClientException(IOException e) {
        e.printStackTrace();
    }
}
