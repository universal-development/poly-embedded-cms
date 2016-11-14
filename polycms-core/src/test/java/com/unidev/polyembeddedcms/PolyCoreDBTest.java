package com.unidev.polyembeddedcms;

import com.unidev.polyembeddedcms.sqlite.SQLitePolyStorage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


public class PolyCoreDBTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private PolyCore polyCore;

    private String tenant = "tenant";

    @Before
    public void setup() {
        polyCore = new PolyCore();
        polyCore.setStorageRoot(temporaryFolder.getRoot());
        polyCore.createTenantStorage(tenant);

        SQLitePolyStorage sqLitePolyStorage = polyCore.fetchSqliteStorage(tenant);
        sqLitePolyStorage.migrateStorage();
    }

    @Test
    public void testDataPersisting() {
        SQLitePolyStorage sqLitePolyStorage = polyCore.fetchSqliteStorage(tenant);

        long count = sqLitePolyStorage.countPoly(new PolyQuery());
        assertThat(count, is(0L));

        PolyRecord polyRecord = new PolyRecord();
        polyRecord._id("potato");
        sqLitePolyStorage.persistPoly(polyRecord);

        long updatedCount = sqLitePolyStorage.countPoly(new PolyQuery());
        assertThat(updatedCount, is(1L));

        List<PolyRecord> listPoly = sqLitePolyStorage.listPoly(new PolyQuery());
        assertThat(listPoly, is(notNullValue()));
        assertThat(listPoly.size(), is(1));

        PolyRecord firstRecord = listPoly.get(0);
        assertThat(firstRecord._id(), is("potato"));

        boolean removeResult = sqLitePolyStorage.removePoly("potato");
        assertThat(removeResult, is(true));

        long countAfterRemoval = sqLitePolyStorage.countPoly(new PolyQuery());
        assertThat(countAfterRemoval, is(0L));

        boolean againRemovalResult = sqLitePolyStorage.removePoly("potato");
        assertThat(againRemovalResult, is(false));
    }

    @Test
    public void testDataListing() {
        SQLitePolyStorage sqLitePolyStorage = polyCore.fetchSqliteStorage(tenant);

        for(int id = 1;id<=10;id++) {
            PolyRecord polyRecord = new PolyRecord();
            polyRecord._id("id_" + id);
            polyRecord.category("category_"  + (id % 2 == 0));
            polyRecord.tags(Arrays.asList("tag_" + id));

            sqLitePolyStorage.persistPoly(polyRecord);
        }

        long count = sqLitePolyStorage.countPoly(new PolyQuery());
        assertThat(count, is(10L));

        PolyQuery categoryQuery = new PolyQuery();
        categoryQuery.setCategory("category_false");
        List<PolyRecord> list = sqLitePolyStorage.listPoly(categoryQuery);
        assertThat(list.size(), is(5));

        PolyQuery notExistingCategoryQuery = new PolyQuery();
        notExistingCategoryQuery.setCategory("tomato_category");
        List<PolyRecord> emptyList = sqLitePolyStorage.listPoly(notExistingCategoryQuery);
        assertThat(emptyList.size(), is(0));

        PolyQuery tagsQuery = new PolyQuery();
        tagsQuery.setTag("tag_10");
        List<PolyRecord> listByTag = sqLitePolyStorage.listPoly(tagsQuery);
        assertThat(listByTag.size(), is(1));

        PolyQuery emptyTagsQuery = new PolyQuery();
        emptyTagsQuery.setTag("potato");
        List<PolyRecord> emptyTagsQueryList = sqLitePolyStorage.listPoly(emptyTagsQuery);
        assertThat(emptyTagsQueryList.size(), is(0));

    }

}
