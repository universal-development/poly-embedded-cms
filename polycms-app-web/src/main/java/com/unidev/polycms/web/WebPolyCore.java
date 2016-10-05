package com.unidev.polycms.web;

import com.unidev.platform.common.utils.StringUtils;
import com.unidev.platform.j2ee.common.WebUtils;
import com.unidev.polydata.SQLiteStorage;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polydata.domain.Poly;
import com.unidev.polyembeddedcms.PolyConstants;
import com.unidev.polyembeddedcms.PolyCore;
import javax.servlet.http.HttpServletRequest;

import com.unidev.polyembeddedcms.PolyRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Core service for web app
 */
@Service
public class WebPolyCore {

    private static Logger LOG = LoggerFactory.getLogger(WebPolyCore.class);

    private PolyCore polyCore;

    private WebUtils webUtils;

    public static Integer ITEM_PER_PAGE = 20;

    public WebPolyCore( @Autowired PolyCore polyCore, @Autowired WebUtils webUtils) {
        this.polyCore = polyCore;
        this.webUtils = webUtils;
    }

    /**
     * List polys filtered by page, category, tag and ordered by date
     * @param page
     * @param category
     * @param tag
     * @param httpServletRequest
     * @return
     */
    public List<BasicPoly> listNewPoly(Integer page, String category, String tag, HttpServletRequest httpServletRequest) {
        SQLiteStorage sqLiteStorage = fetchSqliteDB(httpServletRequest);
        PreparedStatement preparedStatement;
        try(Connection connection = sqLiteStorage.openDb()) {
            Integer id = 1;
            Map<Integer, Object> params = new HashMap<>();

            StringBuilder query = new StringBuilder("SELECT * FROM " + PolyConstants.DATA_POLY + " WHERE 1=1 ");

            if (!StringUtils.isBlank(category)) {
                query.append(  " AND " + PolyConstants.CATEGORY_KEY + " = ?");
                params.put(id++, category );
            }

            if (!StringUtils.isBlank(tag)) {
                query.append(  " AND " + PolyConstants.TAGS_KEY + " LIKE ?");
                params.put(id++, tag );
            }

            query.append(" ORDER BY date DESC LIMIT ? OFFSET ?");
            params.put(id++, ITEM_PER_PAGE);
            params.put(id++, ITEM_PER_PAGE * page);

            preparedStatement = connection.prepareStatement(query.toString());
            for(Map.Entry<Integer, Object> entry : params.entrySet()) {
                preparedStatement.setObject(entry.getKey(), entry.getValue());
            }

            List<BasicPoly> polyList = sqLiteStorage.evaluateStatement(preparedStatement);
            return polyList;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.warn("Failed to fetch categories for request {}", httpServletRequest);
            return null;
        }
    }

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
            preparedStatement = connection.prepareStatement("SELECT * FROM " + PolyConstants.TAGS_POLY + " ORDER BY count DESC");
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
    public PolyRecord fetchPoly(String id, HttpServletRequest httpServletRequest) {
        SQLiteStorage sqLiteStorage = fetchSqliteDB(httpServletRequest);

        PreparedStatement preparedStatement;
        try(Connection connection = sqLiteStorage.openDb()) {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + PolyConstants.DATA_POLY + " WHERE _id = ?");
            preparedStatement.setObject(1, id);
            List<BasicPoly> polyList = sqLiteStorage.evaluateStatement(preparedStatement);
            if (polyList.size() != 1) {
                return null;
            }
            return new PolyRecord(polyList.get(0));
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
