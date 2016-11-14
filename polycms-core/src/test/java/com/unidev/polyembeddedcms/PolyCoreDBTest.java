package com.unidev.polyembeddedcms;

import com.unidev.polyembeddedcms.sqlite.SQLitePolyStorage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

        Optional<PolyRecord> poly5 = sqLitePolyStorage.fetchPoly("id_" + 5);
        PolyRecord poly5Record = poly5.get();
        poly5Record.category("updated_category");
        poly5Record.tags(Arrays.asList("tomato", "potato"));
        poly5Record.put("custom1", "value1");

        sqLitePolyStorage.persistPoly(poly5Record);

        Optional<PolyRecord> updatedPoly5 = sqLitePolyStorage.fetchPoly("id_" + 5);
        assertThat(updatedPoly5.isPresent(), is(true));
        PolyRecord updatedPoly5Record = updatedPoly5.get();

        assertThat(updatedPoly5Record.category(), is("updated_category"));
        assertThat(updatedPoly5Record.get("custom1"), is("value1"));
        assertThat(updatedPoly5Record.tags(), is(Arrays.asList("tomato", "potato")));
    }


    @Test
    public void testTagsOperations() {
        SQLitePolyStorage sqLitePolyStorage = polyCore.fetchSqliteStorage(tenant);

        for(int id=1;id<=10;id++) {
            PolyRecord tag = new PolyRecord()._id("potato"  + id).label("Potato" + id);
            tag.put("customKey", "customValue" + id);

            sqLitePolyStorage.persistTag(tag);
        }

        assertThat(sqLitePolyStorage.countTags(), is(10L));

        Optional<PolyRecord> potato = sqLitePolyStorage.fetchTag("potato5");
        assertThat(potato.isPresent(), is(true));

        PolyRecord potatoTag = potato.get();

        assertThat(potatoTag._id(), is("potato5"));
        assertThat(potatoTag.label(), is("Potato5"));
        assertThat(potatoTag.get("customKey"), is("customValue5"));

        assertThat(sqLitePolyStorage.removeTag("potato5"), is(true));
        assertThat(sqLitePolyStorage.removeTag("potato5"), is(false));

        Optional<PolyRecord> removedPotato5 = sqLitePolyStorage.fetchTag("potato5");
        assertThat(removedPotato5.isPresent(), is(false));

    }

    @Test
    public void testCategoriesOperations() {
        SQLitePolyStorage sqLitePolyStorage = polyCore.fetchSqliteStorage(tenant);

        for(int id=1;id<=10;id++) {
            PolyRecord category = new PolyRecord()._id("potato"  + id).label("Potato" + id);
            category.put("customKey", "customValue" + id);
            sqLitePolyStorage.persistCategory(category);
        }

        assertThat(sqLitePolyStorage.countCategories(), is(10L));

        Optional<PolyRecord> potato = sqLitePolyStorage.fetchCategory("potato2");
        assertThat(potato.isPresent(), is(true));

        PolyRecord potatoCategory = potato.get();

        assertThat(potatoCategory._id(), is("potato2"));
        assertThat(potatoCategory.label(), is("Potato2"));
        assertThat(potatoCategory.get("customKey"), is("customValue2"));

        assertThat(sqLitePolyStorage.removeCategory("potato2"), is(true));
        assertThat(sqLitePolyStorage.removeCategory("potato2"), is(false));

        Optional<PolyRecord> removedPotato2 = sqLitePolyStorage.fetchCategory("potato2");
        assertThat(removedPotato2.isPresent(), is(false));

    }

}
