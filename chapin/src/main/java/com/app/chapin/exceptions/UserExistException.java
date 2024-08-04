package com.app.chapin.exceptions;

public class UserExistException extends Exception {

    public UserExistException(String mensaje) {
        super(mensaje);
    }
}
