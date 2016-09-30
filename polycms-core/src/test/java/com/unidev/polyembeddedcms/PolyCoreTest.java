package com.unidev.polyembeddedcms;


import com.unidev.polydata.FlatFileStorage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class PolyCoreTest {

    private PolyCore polyCore;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setup() {
        polyCore = new PolyCore();
        polyCore.setStorageRoot(temporaryFolder.getRoot().getAbsolutePath());
        polyCore.createIfNotExistTenantIndexFile();
    }

    @Test
    public void testPolyTenantAddRemove() {
        String tenant = "tenant";
        FlatFileStorage flatFileStorage = polyCore.fetchTenantIndex();
        assertThat(flatFileStorage.hasPoly(tenant), is(false));

        polyCore.createTenantStorage(tenant);

        flatFileStorage = polyCore.fetchTenantIndex();
        assertThat(flatFileStorage.hasPoly(tenant), is(true));

        // twice adding
        polyCore.createTenantStorage(tenant);

        flatFileStorage = polyCore.fetchTenantIndex();
        assertThat(flatFileStorage.hasPoly(tenant), is(true));

        polyCore.removeTenantStorage(tenant);

        flatFileStorage = polyCore.fetchTenantIndex();
        assertThat(flatFileStorage.hasPoly(tenant), is(false));

        // twice removal, test if things remains as is

        polyCore.removeTenantStorage(tenant);

        flatFileStorage = polyCore.fetchTenantIndex();
        assertThat(flatFileStorage.hasPoly(tenant), is(false));
    }



}
