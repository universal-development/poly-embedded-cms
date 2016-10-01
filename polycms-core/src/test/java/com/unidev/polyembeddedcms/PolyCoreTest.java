package com.unidev.polyembeddedcms;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PolyCoreTest {

    private PolyCore polyCore;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setup() {
        polyCore = new PolyCore();
        polyCore.setStorageRoot(temporaryFolder.getRoot());
    }

    @Test
    public void testPolyTenantAddRemove() {
        String tenant = "tenant";

        assertThat(polyCore.existTenant(tenant), is(false));

        polyCore.createTenantStorage(tenant);

        assertThat(polyCore.existTenant(tenant), is(true));

        // twice adding
        polyCore.createTenantStorage(tenant);

        assertThat(polyCore.existTenant(tenant), is(true));

        polyCore.removeTenantStorage(tenant);

        assertThat(polyCore.existTenant(tenant), is(false));

        // twice removal, test if things remains as is

        polyCore.removeTenantStorage(tenant);

        assertThat(polyCore.existTenant(tenant), is(false));

    }



}
