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

import com.unidev.polydata.FlatFileStorage;
import com.unidev.polydata.FlatFileStorageMapper;
import com.unidev.polyembeddedcms.sqlite.SQLitePolyStorage;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.unidev.polydata.FlatFileStorageMapper.storageMapper;

/**
 * PolyCore - Service for poly tenants management
 */
public class PolyCore {

    private static Logger LOG = LoggerFactory.getLogger(PolyCore.class);

    private File storageRoot;

    // specific tenant operations

    /**
     * Fetch storage root file for specific tenant
     * @param tenantName
     * @return
     */
    public File fetchStorageRoot(String tenantName) {
        File storageRootFile = new File(storageRoot, tenantName);
        return storageRootFile;
    }

    // Tenant index operations

    /**
     * Check if tenant storage exist
     * @param tenantName
     * @return
     */
    public boolean existTenant(String tenantName) {
        File tenantRoot = fetchStorageRoot(tenantName);
        return tenantRoot.exists();
    }

    /**
     * Register a storage for specific tenant
     * @param tenantName
     */
    public void createTenantStorage(String tenantName) {
        File tenantRoot = fetchStorageRoot(tenantName);
        if (tenantRoot.exists()) {
            LOG.warn("Tenant index already have tenant {}", tenantName);
            return;
        }

        tenantRoot.mkdirs();
        FlatFileStorage flatFileStorage = new FlatFileStorage();
        flatFileStorage.metadata().put("tenant", tenantName);
        FlatFileStorageMapper.storageMapper().saveSource(new File(tenantRoot, PolyConstants.FLAT_FILE_DB)).save(flatFileStorage);
    }

    /**
     * Remove tenant from tenant index
     * @param tenantName
     */
    public void removeTenantStorage(String tenantName) {
        File tenantRoot = fetchStorageRoot(tenantName);
        if (!tenantRoot.exists()) {
            LOG.warn("Tenant index don't have tenant {}", tenantName);
            return;
        }
        FileUtils.deleteQuietly(tenantRoot);
    }

    /**
     * Fetch sqlite storage
     * @param tenant
     * @return
     */
    public SQLitePolyStorage fetchSqliteStorage(String tenant) {
        File dbFile = fetchSqliteFile(tenant);
        return new SQLitePolyStorage(dbFile.getAbsolutePath());
    }

    /**
     * Fetch sqlite file
     * @param tenant
     * @return
     */
    public File fetchSqliteFile(String tenant) {
        File tenantRoot = fetchStorageRoot(tenant);
        return new File(tenantRoot, PolyConstants.DB_FILE);
    }

    /**
     * Fetch flat file storage
     * @param tenant
     * @return
     */
    public FlatFileStorage fetchFlatFileStorage(String tenant) {
        File flatFileDB = fetchFlatFile(tenant);
        if (!flatFileDB.exists()) {
            return new FlatFileStorage();
        }
        return storageMapper().loadSource(flatFileDB).load();
    }


    public File fetchFlatFile(String tenant) {
        File tenantRoot = fetchStorageRoot(tenant);
        return new File(tenantRoot, PolyConstants.FLAT_FILE_DB);
    }

    /**
     * Persist flat file storage
     * @param tenant
     * @param flatfileStorage
     */
    public void persistFlatFileStorage(String tenant, FlatFileStorage flatfileStorage) {
        File flatFileDB = fetchFlatFile(tenant);
        storageMapper().saveSource(flatFileDB).save(flatfileStorage);
    }

    /**
     * List available storages
     * @return
     */
    public List<String> listTenants() {
        return Arrays.asList(storageRoot.list());
    }

    //

    public File getStorageRoot() {
        return storageRoot;
    }

    public void setStorageRoot(File storageRoot) {
        this.storageRoot = storageRoot;
    }
}
