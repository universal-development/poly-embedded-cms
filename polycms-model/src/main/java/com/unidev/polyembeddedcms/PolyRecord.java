package com.unidev.polyembeddedcms;


import com.unidev.polydata.domain.BasicPoly;
import static com.unidev.polyembeddedcms.PolyConstants.*;

/**
 * Basic poly record managed by CMS
 */
public class PolyRecord extends BasicPoly {

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

    public <T> T count() {
        return fetch(COUNT_KEY);
    }

    public <T> PolyRecord count(T count) {
        put(COUNT_KEY, count);
        return this;
    }

    public <T> T date() {
        return fetch(DATA_KEY);
    }

    public <T> PolyRecord date(T date) {
        put(DATA_KEY, date);
        return this;
    }

}
