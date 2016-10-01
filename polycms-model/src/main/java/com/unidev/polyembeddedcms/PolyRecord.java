package com.unidev.polyembeddedcms;


import com.unidev.polydata.domain.BasicPoly;

/**
 * Basic poly record managed by CMS
 */
public class PolyRecord extends BasicPoly {

    public static final String LABEL_KEY = "label";
    public static final String DATA_KEY = "data";

    public PolyRecord _id(String _id) {
        super._id(_id);
        return this;
    }

    public String label() {
        return fetch(LABEL_KEY, null);
    }

    public PolyRecord label(String label) {
        put(LABEL_KEY, label);
        return this;
    }

    public <T> T data() {
        return fetch(DATA_KEY, null);
    }

    public <T> PolyRecord data(T data) {
        put(DATA_KEY, data);
        return this;
    }



}
