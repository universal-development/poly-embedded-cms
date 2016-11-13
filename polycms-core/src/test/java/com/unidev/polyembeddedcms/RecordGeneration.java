package com.unidev.polyembeddedcms;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.unidev.polydata.SQLiteStorage;
import com.unidev.polydata.SQLiteStorageException;
import com.unidev.polyembeddedcms.sqlite.SQLitePolyService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

@Ignore
public class RecordGeneration {

    private PolyCore polyCore;

    private SQLitePolyService sqlitePolyService;

    private File root;
    private String tenant = "localhost";
    private File dbFile;

    @Before
    public void init() {
        polyCore = new PolyCore();
        root = polyCore.fetchStorageRoot(tenant);
        root.mkdirs();

        dbFile = new File(root, PolyConstants.DB_FILE);

        sqlitePolyService = new SQLitePolyService(polyCore);
    }

    @Test
    public void testRecordGeneration() throws MalformedURLException, SQLiteStorageException, JsonProcessingException {
        SQLiteStorage sqLiteStorage = new SQLiteStorage(dbFile.getAbsolutePath());
        sqLiteStorage.setPolyMigrators(Arrays.asList(new TagsPolyMigrator(), new CategoryPolyMigrator(), new DataPolyMigrator()));

        PolyRecord category = new PolyRecord()._id("main").label("Main");
        sqLiteStorage.save(PolyConstants.CATEGORY_POLY, category);

        PolyRecord category2 = new PolyRecord()._id("updates").label("Updates");
        sqLiteStorage.save(PolyConstants.CATEGORY_POLY, category2);

        PolyRecord category3 = new PolyRecord()._id("news").label("News");
        sqLiteStorage.save(PolyConstants.CATEGORY_POLY, category3);

        PolyRecord tag = new PolyRecord()._id("tomato").label("Tomato").count(100);
        sqLiteStorage.save(PolyConstants.TAGS_POLY, tag);

        PolyRecord potato = new PolyRecord()._id("potato").label("Potato").count(100);
        sqLiteStorage.save(PolyConstants.TAGS_POLY, potato);

        for(int i = 1;i<1000;i++) {

            String postId = "post_" + i;

            HashMap<String, String> body = new HashMap<>();
            body.put("data", new Date() + "");
            body.put("record", i + "");

            PolyRecord data = new PolyRecord()._id(postId).label("Post " + i).category("updates").tags("potato, tomato").date(new Date());
            String stringData = PolyConstants.POLY_OBJECT_MAPPER.writeValueAsString(body);
            data.data(stringData);

            //data.data();
            sqLiteStorage.save(PolyConstants.DATA_POLY, data);
        }

    }
}
