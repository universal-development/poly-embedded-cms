package com.unidev.polycms.hateoas.controller;


import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.unidev.platform.common.dto.response.Response;

@Controller
public class ControllerErrorHandler {

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
