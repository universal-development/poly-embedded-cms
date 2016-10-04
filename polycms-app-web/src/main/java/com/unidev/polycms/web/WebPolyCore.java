package com.unidev.polycms.web;

import com.unidev.platform.j2ee.common.WebUtils;
import com.unidev.polydata.SQLiteStorage;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polydata.domain.Poly;
import com.unidev.polyembeddedcms.PolyConstants;
import com.unidev.polyembeddedcms.PolyCore;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Core service for web app
 */
@Service
public class WebPolyCore {

    private static Logger LOG = LoggerFactory.getLogger(WebPolyCore.class);

    @Autowired
    private PolyCore polyCore;

    @Autowired
    private WebUtils webUtils;


    /**
     * Fetch categories records
     * @param httpServletRequest
     * @return
     */
    public List<BasicPoly> fetchCategories(HttpServletRequest httpServletRequest) {
        SQLiteStorage sqLiteStorage = fetchSqliteDB(httpServletRequest);

        PreparedStatement preparedStatement;
        try(Connection connection = sqLiteStorage.openDb()) {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + PolyConstants.CATEGORY_POLY);
            List<BasicPoly> polyList = sqLiteStorage.evaluateStatement(preparedStatement);
            return polyList;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.warn("Failed to fetch categories for request {}", httpServletRequest);
            return null;
        }

    }

    /**
     * Fetch tags from poly store
     * @param httpServletRequest
     * @return
     */
    public List<BasicPoly> fetchTags(HttpServletRequest httpServletRequest) {
        SQLiteStorage sqLiteStorage = fetchSqliteDB(httpServletRequest);

        PreparedStatement preparedStatement;
        try(Connection connection = sqLiteStorage.openDb()) {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + PolyConstants.TAGS_POLY);
            List<BasicPoly> polyList = sqLiteStorage.evaluateStatement(preparedStatement);
            return polyList;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.warn("Failed to fetch tags for request {}", httpServletRequest);
            return null;
        }

    }

    /**
     * Fetch poly by ID
     * @param id
     * @param httpServletRequest
     * @return poly instance or null if poly is not available
     */
    public Poly fetchPoly(String id, HttpServletRequest httpServletRequest) {
        SQLiteStorage sqLiteStorage = fetchSqliteDB(httpServletRequest);

        PreparedStatement preparedStatement;
        try(Connection connection = sqLiteStorage.openDb()) {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + PolyConstants.DATA_POLY + " WHERE _id = ?");
            preparedStatement.setObject(1, id);
            List<BasicPoly> polyList = sqLiteStorage.evaluateStatement(preparedStatement);
            if (polyList.size() != 1) {
                return null;
            }
            return polyList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.warn("Failed to fetch poly {} for request {}", id, httpServletRequest);
            return null;
        }
    }

    /**
     * Fetch tenant root file
     * @param httpServletRequest
     * @return
     */
    public File fetchTenantRoot(HttpServletRequest httpServletRequest) {
        String domain = webUtils.getDomain(httpServletRequest);
        return polyCore.fetchStorageRoot(domain);
    }


    public boolean existsTenant(HttpServletRequest httpServletRequest) {
        return fetchTenantRoot(httpServletRequest).exists();
    }

    private SQLiteStorage fetchSqliteDB(HttpServletRequest httpServletRequest) {
        File tenantRoot = fetchTenantRoot(httpServletRequest);
        File dbFile = new File(tenantRoot, PolyConstants.DB_FILE);
        return new SQLiteStorage(dbFile.getAbsolutePath());
    }

}
