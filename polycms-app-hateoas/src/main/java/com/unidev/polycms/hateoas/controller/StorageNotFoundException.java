package com.unidev.polycms.hateoas.controller;

import com.unidev.platform.common.exception.CommonRuntimeException;

/**
 * Exception thrown if requested storage is not found
 */
public class StorageNotFoundException extends CommonRuntimeException {

    public StorageNotFoundException(String message) {
        super(message);
    }
}
