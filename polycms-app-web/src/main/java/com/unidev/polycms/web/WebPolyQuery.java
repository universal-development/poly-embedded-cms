package com.unidev.polycms.web;

import com.unidev.polyembeddedcms.PolyQuery;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.ui.Model;

/**
 * Wrapper class for polydata web requests
 */
public class WebPolyQuery extends PolyQuery {

    private String polyId;
    private HttpServletRequest request;
    private Model model;

    public static WebPolyQuery polyRequest() {
        return new WebPolyQuery();
    }


    public String polyId() {
        return polyId;
    }

    public WebPolyQuery polyId(String polyId) {
        this.polyId = polyId;
        return this;
    }

    public Model model() {
        return model;
    }

    public WebPolyQuery model(Model model) {
        this.model = model;
        return this;
    }

    public HttpServletRequest request() {
        return request;
    }

    public WebPolyQuery request(HttpServletRequest request) {
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

    public String getPolyId() {
        return polyId;
    }

    public void setPolyId(String polyId) {
        this.polyId = polyId;
    }
}
