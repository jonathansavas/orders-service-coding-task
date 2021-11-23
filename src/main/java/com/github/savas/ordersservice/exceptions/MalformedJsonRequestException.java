package com.github.savas.ordersservice.exceptions;

public class MalformedJsonRequestException extends RuntimeException {
    public MalformedJsonRequestException(String message) {
        super(message);
    }
}
