package com.unidev.polyembeddedcms;


import com.unidev.polydata.domain.BasicPoly;

import java.io.IOException;
import java.util.Map;

import static com.unidev.polyembeddedcms.PolyConstants.*;

/**
 * Basic poly record managed by CMS
 */
public class PolyRecord extends BasicPoly {

    public PolyRecord() {}

    public PolyRecord(BasicPoly basicPoly) {
        putAll(basicPoly);
    }

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

    public Map jsonData() {
        String data = data();
        try {
            return PolyConstants.POLY_OBJECT_MAPPER.readValue(data, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T count() {
        return fetch(COUNT_KEY);
    }

    public <T> PolyRecord count(T count) {
        put(COUNT_KEY, count);
        return this;
    }

    public <T> T date() {
        return fetch(DATE_KEY);
    }

    public <T> PolyRecord date(T date) {
        put(DATE_KEY, date);
        return this;
    }

    public <T> T category() {
        return fetch(CATEGORY_KEY);
    }

    public <T> PolyRecord category(T category) {
        put(CATEGORY_KEY, category);
        return this;
    }

    public <T> T tags() {
        return fetch(TAGS_KEY);
    }

    public <T> PolyRecord tags(T tags) {
        put(TAGS_KEY, tags);
        return this;
    }



}
