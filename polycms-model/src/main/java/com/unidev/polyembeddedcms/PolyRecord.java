package com.unidev.polyembeddedcms;


import com.unidev.polydata.domain.BasicPoly;

/**
 * Basic poly record managed by CMS
 */
public class PolyRecord extends BasicPoly {

    public static final String LABEL_KEY = "label";
    public static final String DATA_KEY = "data";

    public String fetchLabel() {
        return fetch(LABEL_KEY, null);
    }

    public PolyRecord putLabel(String label) {
        put(LABEL_KEY, label);
        return this;
    }

    public <T> T fetchData() {
        return fetch(DATA_KEY, null);
    }

    public <T> PolyRecord putData(T data) {
        put(DATA_KEY, data);
        return this;
    }



}
