package com.unidev.polycms.hateoas.vo;


import org.springframework.hateoas.ResourceSupport;

public class HateoasResponse<T> extends ResourceSupport {

    T data;

    public static HateoasResponse hateoasResponse() {
        return new HateoasResponse();
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public HateoasResponse data(T data) {
        this.data = data;
        return this;
    }
}
