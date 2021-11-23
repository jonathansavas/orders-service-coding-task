package com.github.savas.ordersservice.exceptions;

public class NoItemsAddedException extends RuntimeException {
    public NoItemsAddedException(String message) {
        super(message);
    }
}
