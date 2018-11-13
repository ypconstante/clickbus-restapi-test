package com.clickbus.service.web.rest.errors;

public class InvalidDataException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public InvalidDataException(String message, String entity) {
        super(ErrorConstants.DATA_NOT_FOUND_TYPE, message, entity, "invaliddata");
    }
}
