package com.dimu.dimuapi.exceptionshandling;

public class CustomException extends RuntimeException{

    public CustomException(String errorMessage){
        super(errorMessage);
    }
}
