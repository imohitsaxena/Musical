package com.techilse.mohit.musical.exception;

/**
 * Created by Mohit on 11-10-2014.
 */
public class AppException extends Exception{
    public AppException() {
    }

    public AppException(String detailMessage) {
        super(detailMessage);
    }

    public AppException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AppException(Throwable throwable) {
        super(throwable);
    }
}
