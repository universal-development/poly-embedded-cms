package com.unidev.polyembeddedcms;

import com.unidev.polydata.FlatFileStorage;
import com.unidev.polydata.FlatFileStorageMapper;
import com.unidev.polydata.domain.BasicPoly;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Service for poly tenants management
 */
@Service
public class PolyCore {
    public static final String INDEX_FILE = "index.json";
    private String storageRoot;

    public File fetchStorageRoot(String tenantName) {
        File storageFile = new File(storageRoot + "/" + tenantName);
        return storageFile;
    }

    public void createTenantSorage(String tenantName) {
        File tenantIndexFile = new File(storageRoot + "/" + INDEX_FILE);

        FlatFileStorage tenantIndex =
                FlatFileStorageMapper.storageMapper().loadSource(tenantIndexFile).load();

        tenantIndex.add(new BasicPoly()._id(tenantName));

        FlatFileStorageMapper.storageMapper().saveSource(tenantIndexFile).save(tenantIndex);
    }

    // ========================

    public String getStorageRoot() {
        return storageRoot;
    }

    public void setStorageRoot(String storageRoot) {
        this.storageRoot = storageRoot;
    }
}
