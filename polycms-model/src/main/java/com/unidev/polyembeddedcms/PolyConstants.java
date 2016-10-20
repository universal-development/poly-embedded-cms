package com.unidev.polyembeddedcms;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Constant keys used in poly records
 */
public class PolyConstants {

    public static final String INDEX_FILE = "index.json";
    public static final String DB_FILE = "polydata.db";
    public static final String FLAT_FILE_DB = "polydata.json";

    public static final String ID_KEY = "_id";
    public static final String DATE_KEY = "date";
    public static final String CATEGORY_KEY = "category";
    public static final String TAGS_KEY = "tags";
    public static final String DATA_KEY = "data";
    public static final String LABEL_KEY = "label";
    public static final String COUNT_KEY = "count";

    public static final String ITEM_PER_PAGE_KEY = "item_per_page";


    public static final String CATEGORY_POLY = "category";
    public static final String TAGS_POLY = "tag";
    public static final String DATA_POLY = "data";

    public static ObjectMapper POLY_OBJECT_MAPPER = new ObjectMapper() {{
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }};

}
