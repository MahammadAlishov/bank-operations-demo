package com.bank.operation.exception;

public class NotFoundException extends RuntimeException {

    private static final String DEFAULT_KEYWORD = " not found by this ";

    public NotFoundException(Class clazz, Long id) {
        super(clazz.getSimpleName().concat(DEFAULT_KEYWORD).concat("id : ").concat(id.toString()));
    }

    public NotFoundException(Class clazz, String number) {
        super(clazz.getSimpleName().concat(DEFAULT_KEYWORD).concat("number : ").concat(number));
    }
}
