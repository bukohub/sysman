package com.sysman.orden.domain.exception;

public abstract class DominioException extends RuntimeException {

    protected DominioException(String message) {
        super(message);
    }
}
