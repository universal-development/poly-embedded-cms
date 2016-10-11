package com.unidev.polycms.web;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.ui.Model;

/**
 * Wrapper class for polydata web requests
 */
public class PolyRequest {

    private HttpServletRequest request;
    private Model model;

    public static PolyRequest polyRequest() {
        return new PolyRequest();
    }

    public Model model() {
        return model;
    }

    public PolyRequest model(Model model) {
        this.model = model;
        return this;
    }

    public HttpServletRequest request() {
        return request;
    }

    public PolyRequest request(HttpServletRequest request) {
        this.request = request;
        return this;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
