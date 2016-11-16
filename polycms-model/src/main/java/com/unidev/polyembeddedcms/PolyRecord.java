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
package com.unidev.polyembeddedcms;


import com.unidev.polydata.domain.BasicPoly;

import java.io.IOException;
import java.util.List;
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

    public Map getJsonData() {
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

    public String category() {
        return fetch(CATEGORY_KEY);
    }

    public PolyRecord category(String category) {
        put(CATEGORY_KEY, category);
        return this;
    }

    public List<String> tags() {
        return fetch(TAGS_KEY);
    }

    public PolyRecord tags(List<String> tags) {
        put(TAGS_KEY, tags);
        return this;
    }



}
