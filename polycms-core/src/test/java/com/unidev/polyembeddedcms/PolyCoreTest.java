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
        temporaryFolder.getRoot().mkdirs();
        polyCore.setStorageRoot(temporaryFolder.getRoot().getAbsolutePath());
    }

    @Test
    public void testPolyTenantAdding() {

        String tenant = "tenan";

        FlatFileStorage flatFileStorage = polyCore.fetchTenantIndex();
        assertThat(flatFileStorage.hasPoly(tenant), is(false));

        polyCore.createTenantSorage(tenant);

        flatFileStorage = polyCore.fetchTenantIndex();

        assertThat(flatFileStorage.hasPoly(tenant), is(true));

    }

}
