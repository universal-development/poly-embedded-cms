package com.unidev.polyembeddedcms;

import com.unidev.polydata.FlatFileStorage;
import com.unidev.polydata.FlatFileStorageMapper;
import com.unidev.polydata.domain.BasicPoly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * PolyCore - Service for poly tenants management
 */
@Service
public class PolyCore {

    private static Logger LOG = LoggerFactory.getLogger(PolyCore.class);

    public static final String INDEX_FILE = "index.json";

    private String storageRoot;

    // specific tenant operations

    /**
     * Fetch storage root file for specific tenant
     * @param tenantName
     * @return
     */
    public File fetchStorageRoot(String tenantName) {
        File storageFile = new File(storageRoot + "/" + tenantName);
        return storageFile;
    }

    // Tenant index operations

    /**
     * Fetch tenants indexes file
     * @return
     */
    public File fetchTenantIndexFile() {
        return new File(storageRoot + "/" + INDEX_FILE);
    }

    /**
     * Create tenant index file
     */
    public void createIfNotExistTenantIndexFile() {
        File file = fetchTenantIndexFile();
        if (file.exists()) {
            return;
        }
        FlatFileStorageMapper.storageMapper().saveSource(file).save(new FlatFileStorage());
    }

    /**
     * Fetch tenant index storage
     * @return
     */
    public FlatFileStorage fetchTenantIndex() {
        File tenantIndexFile = fetchTenantIndexFile();
        FlatFileStorage tenantIndex =
                FlatFileStorageMapper.storageMapper().loadSource(tenantIndexFile).load();
        return tenantIndex;
    }

    /**
     * Register a storage for specific tenant
     * @param tenantName
     */
    public void createTenantStorage(String tenantName) {
        FlatFileStorage tenantIndex = fetchTenantIndex();
        if (tenantIndex.hasPoly(tenantName)) {
            return;
        }
        tenantIndex.add(new BasicPoly()._id(tenantName).link(tenantName));
        FlatFileStorageMapper.storageMapper().saveSource(fetchTenantIndexFile()).save(tenantIndex);

        fetchStorageRoot(tenantName).mkdirs();
    }

    /**
     * Remove tenant from tenant index
     * @param tenantName
     */
    public void removeTenantStorage(String tenantName) {
        FlatFileStorage tenantIndex = fetchTenantIndex();
        if (!tenantIndex.hasPoly(tenantName)) {
            return;
        }
        tenantIndex.remove(tenantName);
        FlatFileStorageMapper.storageMapper().saveSource(fetchTenantIndexFile()).save(tenantIndex);
    }

    //

    public String getStorageRoot() {
        return storageRoot;
    }

    public void setStorageRoot(String storageRoot) {
        this.storageRoot = storageRoot;
    }
}
