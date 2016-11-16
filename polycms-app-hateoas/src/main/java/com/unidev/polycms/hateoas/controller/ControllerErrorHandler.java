/**
 * Copyright (c) 2016 Denis O <denis@universal-development.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unidev.polycms.hateoas.controller;


import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.unidev.platform.common.dto.response.Response;

/**
 * Handling of different types of exceptions
 */
@ControllerAdvice
public class ControllerErrorHandler {

    private static Logger LOG = LoggerFactory.getLogger(ControllerErrorHandler.class);


    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response serviceError(HttpServletRequest req, Throwable e) {
        LOG.error("Error in handling request {}", req, e);
        Response<String, String> response = new Response<>();
        response.setPayload("Service error");
        response.setStatus("service-error");
        return response;
    }

    @ExceptionHandler(StorageNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public Response handleNotFoundError(HttpServletRequest req) {
        Response<String, String> response = new Response<>();
        response.setPayload("Not found");
        response.setStatus("not-found");
        return response;
    }

}
