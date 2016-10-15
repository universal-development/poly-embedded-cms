package com.unidev.polyembeddedcms;


import com.unidev.polydata.SQLiteStorage;
import com.unidev.polydata.domain.BasicPoly;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for managing poly records
 */
@Service
public class SQLitePolyService {

    private static Logger LOG = LoggerFactory.getLogger(SQLitePolyService.class);

    private PolyCore polyCore;

    public SQLitePolyService(PolyCore polyCore) {
        this.polyCore = polyCore;
    }

    /**
     * List polys filtered by page, category, tag and ordered by date
     * @return
     */
    public List<BasicPoly> listNewPoly(PolyQuery listNewPolyQuery, String tenant) {
        SQLiteStorage sqLiteStorage = fetchSqliteDB(tenant);
        PreparedStatement preparedStatement;
        try(Connection connection = sqLiteStorage.openDb()) {
            StringBuilder query = new StringBuilder("SELECT * FROM " + PolyConstants.DATA_POLY + " WHERE 1=1 ");
            preparedStatement = buildPolyQuery(listNewPolyQuery, connection, query);

            List<BasicPoly> polyList = sqLiteStorage.evaluateStatement(preparedStatement);
            return polyList;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.warn("Failed to fetch polys for tenant {}", tenant);
            return Collections.EMPTY_LIST;
        }
    }


    /**
     * Count polys by poly categories
     * @param polyQuery
     * @param tenant
     * @return
     */
    public Long countPoly(PolyQuery polyQuery, String tenant) {
        SQLiteStorage sqLiteStorage = fetchSqliteDB(tenant);
        PreparedStatement preparedStatement;
        try(Connection connection = sqLiteStorage.openDb()) {
            StringBuilder query = new StringBuilder("SELECT COUNT(*) AS polys FROM " + PolyConstants.DATA_POLY + " WHERE 1=1 ");
            preparedStatement = buildPolyQuery(polyQuery, connection, query);
            return Long.parseLong(sqLiteStorage.evaluateStatement(preparedStatement).get(0).get("polys") + "");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.warn("Failed to count polys {}", tenant);
            return 0L;
        }
    }

    /**
     * Generic method to build query on stored polys
     */
    private PreparedStatement buildPolyQuery(PolyQuery listNewPolyQuery, Connection connection, StringBuilder query) throws SQLException {
        Integer id = 1;
        Map<Integer, Object> params = new HashMap<>();
        PreparedStatement preparedStatement;
        if (!StringUtils.isBlank(listNewPolyQuery.getCategory())) {
            query.append(  " AND " + PolyConstants.CATEGORY_KEY + " = ?");
            params.put(id++, listNewPolyQuery.getCategory() );
        }

        if (!StringUtils.isBlank(listNewPolyQuery.getTag())) {
            query.append(  " AND " + PolyConstants.TAGS_KEY + " LIKE ?");
            params.put(id++, "%" + listNewPolyQuery.getTag() + "%" );
        }

        if (listNewPolyQuery.getItemPerPage() != null ) {
            query.append(" ORDER BY date DESC LIMIT ? OFFSET ?");
            params.put(id++, listNewPolyQuery.getItemPerPage());
            params.put(id++, listNewPolyQuery.getItemPerPage() * listNewPolyQuery.getPage());
        }

        preparedStatement = connection.prepareStatement(query.toString());
        for(Map.Entry<Integer, Object> entry : params.entrySet()) {
            preparedStatement.setObject(entry.getKey(), entry.getValue());
        }
        return preparedStatement;
    }

    /**
     * Fetch categories records
     * @return
     */
    public List<BasicPoly> fetchCategories(String tenant) {
        SQLiteStorage sqLiteStorage = fetchSqliteDB(tenant);

        PreparedStatement preparedStatement;
        try(Connection connection = sqLiteStorage.openDb()) {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + PolyConstants.CATEGORY_POLY);
            List<BasicPoly> polyList = sqLiteStorage.evaluateStatement(preparedStatement);
            return polyList;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.warn("Failed to fetch categories for tenant {}", tenant);
            return null;
        }

    }

    /**
     * Fetch tags from poly store
     * @return
     */
    public List<BasicPoly> fetchTags(String tenant) {
        SQLiteStorage sqLiteStorage = fetchSqliteDB(tenant);

        PreparedStatement preparedStatement;
        try(Connection connection = sqLiteStorage.openDb()) {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + PolyConstants.TAGS_POLY + " ORDER BY count DESC");
            List<BasicPoly> polyList = sqLiteStorage.evaluateStatement(preparedStatement);
            return polyList;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.warn("Failed to fetch tags for tenant {}", tenant);
            return null;
        }

    }

    /**
     * Fetch poly by ID
     * @param id
     * @return poly instance or null if poly is not available
     */
    public PolyRecord fetchPoly(String id, String tenant) {
        SQLiteStorage sqLiteStorage = fetchSqliteDB(tenant);

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
            LOG.warn("Failed to fetch poly {} for tenant {}", id, tenant);
            return null;
        }
    }

    /**
     * Fetch tenant root file
     * @return
     */
    public File fetchTenantRoot(String tenant) {
        return polyCore.fetchStorageRoot(tenant);
    }


    public boolean existsTenant(String tenant) {
        return fetchTenantRoot(tenant).exists();
    }

    private SQLiteStorage fetchSqliteDB(String tenant) {
        File tenantRoot = fetchTenantRoot(tenant);
        File dbFile = new File(tenantRoot, PolyConstants.DB_FILE);
        return new SQLiteStorage(dbFile.getAbsolutePath());
    }


}
