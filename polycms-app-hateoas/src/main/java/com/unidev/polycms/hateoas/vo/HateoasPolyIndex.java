package com.unidev.polycms.hateoas.vo;


import org.springframework.hateoas.ResourceSupport;

public class HateoasPolyIndex<T> extends ResourceSupport {

    T data;

    public static HateoasPolyIndex hateoasPolyIndex() {
        return new HateoasPolyIndex();
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public HateoasPolyIndex data(T data) {
        this.data = data;
        return this;
    }
}
