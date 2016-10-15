package com.unidev.polyembeddedcms;

import com.unidev.polydata.SQLiteStorage;
import com.unidev.polydata.SQLiteStorageException;
import com.unidev.polydata.domain.BasicPoly;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.unidev.polyembeddedcms.PolyQuery.query;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

/**
 * SQLite poly service testing
 */
public class TestSQLitePolyService {

    private PolyCore polyCore;

    private SQLitePolyService sqlitePolyService;

    private File root;
    private String tenant = "test.com";
    private File dbFile;

    @Before
    public void init() {
        polyCore = new PolyCore();
        root = polyCore.fetchStorageRoot(tenant);
        root.mkdirs();

        dbFile = new File(root, PolyConstants.DB_FILE);
        dbFile.deleteOnExit();

        sqlitePolyService = new SQLitePolyService(polyCore);
    }

    @Test
    public void testTagsListing() throws MalformedURLException, SQLiteStorageException {
        SQLiteStorage sqLiteStorage = new SQLiteStorage(dbFile.getAbsolutePath());
        sqLiteStorage.setPolyMigrators(Arrays.asList(new TagsPolyMigrator()));

        PolyRecord tagTomato = new PolyRecord()._id("tomato").label("Tomato").count(100);
        sqLiteStorage.save(PolyConstants.TAGS_POLY, tagTomato);

        PolyRecord tagPotato = new PolyRecord()._id("potato").label("Potato").count(10);
        sqLiteStorage.save(PolyConstants.TAGS_POLY, tagPotato);

        List<BasicPoly> list = sqlitePolyService.fetchTags(tenant);
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));

        BasicPoly first = list.get(0);

        assertThat(first.get(PolyConstants.COUNT_KEY), is(100));
        assertThat(first._id(), is("tomato"));
    }

    @Test
    public void testSinglePolyLoading() throws SQLiteStorageException {
        SQLiteStorage sqLiteStorage = new SQLiteStorage(dbFile.getAbsolutePath());
        sqLiteStorage.setPolyMigrators(Arrays.asList(new DataPolyMigrator()));

        String postId = "post_1";

        PolyRecord data = new PolyRecord()._id(postId).label("Tomato").category("Test").tags("tag1, tag2").date(new Date());
        sqLiteStorage.save(PolyConstants.DATA_POLY, data);

        PolyRecord poly = sqlitePolyService.fetchPoly(postId, tenant);

        assertThat(poly, is(not(nullValue())));
        assertThat(poly._id(), is(postId));
        assertThat(poly.date(), is(notNullValue()));

        PolyRecord notExistingPoly = sqlitePolyService.fetchPoly("not-existing-id", tenant);
        assertThat(notExistingPoly, is(nullValue()));

    }

    @Test
    public void testPolyPagination() throws SQLiteStorageException {
        SQLiteStorage sqLiteStorage = new SQLiteStorage(dbFile.getAbsolutePath());
        sqLiteStorage.setPolyMigrators(Arrays.asList(new DataPolyMigrator()));

        for(int i = 0;i<10;i++) {
            String postId = "post_" + i;

            PolyRecord data = new PolyRecord()._id(postId).label("Label " + i ).category("Cat1").tags("tag1, tag2").date(new Date( System.currentTimeMillis() + 100 * i));
            sqLiteStorage.save(PolyConstants.DATA_POLY, data);
        }

        PolyQuery listNewPolyQuery = query().page(0L).itemPerPage(5L);

        List<BasicPoly> basicPolyList = sqlitePolyService.listNewPoly(listNewPolyQuery, tenant);
        assertThat(basicPolyList, is(notNullValue()));
        assertThat(basicPolyList.size(), is(5));

        PolyQuery tomatoQuery = query().category("Tomato");
        List<BasicPoly> tomatoList = sqlitePolyService.listNewPoly(tomatoQuery, tenant);
        assertThat(tomatoList, is(notNullValue()));
        assertThat(tomatoList.size(), is(0));

        PolyQuery tagsQuery = query().tag("tag1").itemPerPage(2L);
        List<BasicPoly> tagsList = sqlitePolyService.listNewPoly(tagsQuery, tenant);
        assertThat(tagsList, is(notNullValue()));
        assertThat(tagsList.size(), is(2));


    }

}
