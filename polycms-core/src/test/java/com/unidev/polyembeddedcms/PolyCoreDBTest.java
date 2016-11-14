package com.unidev.polyembeddedcms;

import com.unidev.polyembeddedcms.sqlite.SQLitePolyStorage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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

    }

}
