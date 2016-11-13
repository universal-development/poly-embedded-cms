package com.unidev.polyembeddedcms.sqlite;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.unidev.platform.common.utils.StringUtils;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polyembeddedcms.PolyConstants;
import com.unidev.polyembeddedcms.PolyQuery;
import com.unidev.polyembeddedcms.PolyRecord;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * SQLite based poly storage
 */
public class SQLitePolyStorage {

    private static Logger LOG = LoggerFactory.getLogger(SQLitePolyStorage.class);

    public static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            LOG.error("Failed to load SQLite driver", e);
        }
    }

    private String dbFile;

    public SQLitePolyStorage(String dbFile) {
        this.dbFile = dbFile;
    }

    /**
     * Open poly storage connection
     * @return
     * @throws SQLException
     */
    public Connection openDb() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + dbFile);
    }

    /**
     * Migrate storage records
     */
    public void migrateStorage() {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:sqlite:" + dbFile, null, null);
        flyway.setOutOfOrder(true);
        flyway.setLocations("db/sqlite");
        flyway.migrate();
    }

    /**
     * Fetch poly by id
     * @param id
     * @return
     */
    public Optional<PolyRecord> fetchPoly(String id) {

        PreparedStatement preparedStatement;
        try(Connection connection = openDb()) {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + PolyConstants.DATA_POLY + " WHERE _id = ?");
            preparedStatement.setObject(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            String rawJSON = resultSet.getString(PolyConstants.DATA_KEY);
            PolyRecord polyRecord = OBJECT_MAPPER.readValue(rawJSON, PolyRecord.class);
            return Optional.of(polyRecord);
        } catch (Exception e) {
            LOG.warn("Failed to fetch poly {} for tenant {}", id, e);
            return Optional.empty();
        }
    }

    /**
     * Remove poly from db
     * @param id
     * @return
     */
    public boolean removePoly(String id) {
        try(Connection connection = openDb()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + PolyConstants.DATA_POLY + " WHERE _id = ?");
            preparedStatement.setString(1, id);
            return preparedStatement.executeUpdate() != 0;
        } catch (Exception e) {
            LOG.error("Failed to remove poly {}", id, e);
            return false;
        }
    }

    /**
     * List poly
     * @param polyQuery
     * @return
     */
    public List<PolyRecord> listPoly(PolyQuery polyQuery) {
        List<PolyRecord> polyList = new ArrayList<>();

        PreparedStatement preparedStatement;
        try(Connection connection = openDb()) {
            StringBuilder query = new StringBuilder("SELECT * FROM " + PolyConstants.DATA_POLY + " WHERE 1=1 ");
            preparedStatement = buildPolyQuery(polyQuery, connection, query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String rawJSON = resultSet.getString(PolyConstants.DATA_KEY);
                polyList.add(OBJECT_MAPPER.readValue(rawJSON, PolyRecord.class));
            }

            return polyList;
        } catch (Exception e) {
            LOG.warn("Failed to fetch polys", e);
            return Collections.EMPTY_LIST;
        }
    }

    public long countPoly(PolyQuery polyQuery) {
        List<PolyRecord> polyList = new ArrayList<>();

        PreparedStatement preparedStatement;
        try(Connection connection = openDb()) {
            StringBuilder query = new StringBuilder("SELECT COUNT(*) as count FROM " + PolyConstants.DATA_POLY + " WHERE 1=1 ");
            polyQuery.itemPerPage(null).page(null);
            preparedStatement = buildPolyQuery(polyQuery, connection, query);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getLong("count");
        } catch (Exception e) {
            LOG.warn("Failed to fetch polys", e);
            return 0;
        }
    }

    public void persistPoly(PolyRecord poly) {
        try(Connection connection = openDb()) {

            String rawJSON = OBJECT_MAPPER.writeValueAsString(poly);

            PreparedStatement dataStatement = connection.prepareStatement("SELECT * FROM " + PolyConstants.DATA_POLY + " WHERE _id = ?;");
            dataStatement.setString(1, poly._id());
            ResultSet dataResultSet = dataStatement.executeQuery();


            if (!dataResultSet.next()) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT OR REPLACE INTO " + PolyConstants.DATA_POLY + "(_id, category, tags, data) VALUES(?, ?, ?, ?);");
                preparedStatement.setString(1, poly._id());
                preparedStatement.setString(2, poly.category());
                preparedStatement.setString(3, String.join(",",poly.tags()));
                preparedStatement.setObject(4, rawJSON);
                preparedStatement.executeUpdate();
            } else {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT OR REPLACE INTO " + PolyConstants.DATA_POLY + "(id, _id, category, tags, data) VALUES(?, ?, ?, ?, ?);");
                preparedStatement.setObject(1, dataResultSet.getObject("id"));
                preparedStatement.setString(2, poly._id());
                preparedStatement.setString(3, poly.category());
                preparedStatement.setString(4, String.join(",",poly.tags()));
                preparedStatement.setObject(5, rawJSON);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            LOG.error("Failed to import poly {}", poly, e);
        }
    }

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
            query.append(" ORDER BY id DESC LIMIT ? OFFSET ?");
            params.put(id++, listNewPolyQuery.getItemPerPage());
            params.put(id++, listNewPolyQuery.getItemPerPage() * (listNewPolyQuery.getPage() - 1));
        }

        preparedStatement = connection.prepareStatement(query.toString());
        for(Map.Entry<Integer, Object> entry : params.entrySet()) {
            preparedStatement.setObject(entry.getKey(), entry.getValue());
        }
        return preparedStatement;
    }

}
