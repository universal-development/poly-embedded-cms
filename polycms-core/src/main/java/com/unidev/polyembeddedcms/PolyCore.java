package com.unidev.polyembeddedcms;

import com.unidev.polydata.FlatFileStorage;
import com.unidev.polydata.FlatFileStorageMapper;
import com.unidev.polydata.domain.BasicPoly;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * PolyCore - Service for poly tenants management
 */
public class PolyCore {

    private static Logger LOG = LoggerFactory.getLogger(PolyCore.class);

    public static final String INDEX_FILE = "index.json";

    private File storageRoot;

    // specific tenant operations

    /**
     * Fetch storage root file for specific tenant
     * @param tenantName
     * @return
     */
    public File fetchStorageRoot(String tenantName) {
        return new File(storageRoot, tenantName);
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
        FlatFileStorageMapper.storageMapper().saveSource(new File(tenantRoot, INDEX_FILE)).save(flatFileStorage);
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
