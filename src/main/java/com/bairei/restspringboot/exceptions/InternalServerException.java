package com.bairei.restspringboot.exceptions;

public class InternalServerException extends Throwable {

    private Exception exception;

    public InternalServerException(Exception e) {
        this.exception = e;
    }

    public Exception getException() {
        return exception;
    }

}
