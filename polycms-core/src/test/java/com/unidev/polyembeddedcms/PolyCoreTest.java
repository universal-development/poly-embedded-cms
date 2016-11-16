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
