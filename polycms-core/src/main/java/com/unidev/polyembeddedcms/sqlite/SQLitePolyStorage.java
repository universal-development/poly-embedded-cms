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
package com.unidev.polyembeddedcms.sqlite;

import com.unidev.platform.common.utils.StringUtils;
import com.unidev.polyembeddedcms.PolyConstants;
import com.unidev.polyembeddedcms.PolyCoreException;
import com.unidev.polyembeddedcms.PolyQuery;
import com.unidev.polyembeddedcms.PolyRecord;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

import static com.unidev.polyembeddedcms.PolyConstants.POLY_OBJECT_MAPPER;

/**
 * SQLite based poly storage
 */
public class SQLitePolyStorage {

    private static Logger LOG = LoggerFactory.getLogger(SQLitePolyStorage.class);

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
        return fetchRawPoly(PolyConstants.DATA_POLY, id);
    }

    /**
     * Remove poly from db
     * @param id
     * @return
     */
    public boolean removePoly(String id) {
        return removeRawPoly(PolyConstants.DATA_POLY, id);
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
                polyList.add(POLY_OBJECT_MAPPER.readValue(rawJSON, PolyRecord.class));
            }

            return polyList;
        } catch (Exception e) {
            LOG.warn("Failed to fetch polys", e);
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * Count available polys based on polyQuery
     * @param polyQuery
     * @return
     */
    public long countPoly(PolyQuery polyQuery) {
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

    /**
     * Persist poly into db
     * @param poly
     */
    public void persistPoly(PolyRecord poly) {
        try(Connection connection = openDb()) {

            String rawJSON = POLY_OBJECT_MAPPER.writeValueAsString(poly);

            PreparedStatement dataStatement = connection.prepareStatement("SELECT * FROM " + PolyConstants.DATA_POLY + " WHERE _id = ?;");
            dataStatement.setString(1, poly._id());
            ResultSet dataResultSet = dataStatement.executeQuery();


            if (!dataResultSet.next()) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT OR REPLACE INTO " + PolyConstants.DATA_POLY + "(_id, category, tags, data) VALUES(?, ?, ?, ?);");
                preparedStatement.setString(1, poly._id());
                preparedStatement.setString(2, poly.category());
                if (  poly.tags() == null) {
                    preparedStatement.setString(3, null);
                } else {
                    preparedStatement.setString(3, String.join(",", poly.tags()));
                }
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

    // categories


    public List<PolyRecord> listCategories() {
        return fetchSupportPolys(PolyConstants.CATEGORY_POLY);
    }

    public long countCategories() {
        return fetchSupportPolysCount(PolyConstants.CATEGORY_POLY);
    }

    public void persistCategory(PolyRecord polyRecord) {
        persistSupportPoly(PolyConstants.CATEGORY_POLY, polyRecord);
    }

    public Optional<PolyRecord> fetchCategory(String categoryId) {
        return fetchRawPoly(PolyConstants.CATEGORY_POLY, categoryId);
    }

    public boolean removeCategory(String categoryId) {
        return removeRawPoly(PolyConstants.CATEGORY_POLY, categoryId);
    }


    // tags

    public List<PolyRecord> listTags() {
        return fetchSupportPolys(PolyConstants.TAGS_POLY);
    }

    public long countTags() {
        return fetchSupportPolysCount(PolyConstants.TAGS_POLY);
    }

    public void persistTag(PolyRecord polyRecord) {
        persistSupportPoly(PolyConstants.TAGS_POLY, polyRecord);
    }

    public Optional<PolyRecord> fetchTag(String tagId) {
        return fetchRawPoly(PolyConstants.TAGS_POLY, tagId);
    }

    public boolean removeTag(String tagId) {
        return removeRawPoly(PolyConstants.TAGS_POLY, tagId);
    }

    /**
     * Fetch support polys from provided table
     * @param table
     * @return
     */
    public List<PolyRecord> fetchSupportPolys(String table) {
        PreparedStatement preparedStatement;
        try(Connection connection = openDb()) {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " ORDER BY count DESC");
            return evaluateSupportStatement(preparedStatement);
        } catch (Exception e) {
            LOG.warn("Failed to fetch tags for tenant",e);
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * Count available polys from support table
     * @param table
     * @return
     */
    public long fetchSupportPolysCount(String table) {
        PreparedStatement preparedStatement;
        try(Connection connection = openDb()) {
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM " + table + " ORDER BY count DESC");
            return preparedStatement.executeQuery().getLong("count");
        } catch (Exception e) {
            LOG.warn("Failed to count support polys {}", table, e);
            return 0;
        }
    }



    /**
     * Fetch support poly by id
     * @return
     */
     public Optional<PolyRecord> fetchRawPoly(String table, String id) {
         PreparedStatement preparedStatement;
         try(Connection connection = openDb()) {
             preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE _id = ?");
             preparedStatement.setString(1, id);
             ResultSet resultSet = preparedStatement.executeQuery();
             if (resultSet.next()) {
                 String rawJSON = resultSet.getString(PolyConstants.DATA_KEY);
                 return Optional.of(POLY_OBJECT_MAPPER.readValue(rawJSON, PolyRecord.class));
             }
             return Optional.empty();
         } catch (Exception e) {
             LOG.warn("Failed to fetch support poly {} {}", table, id, e);
             return Optional.empty();
         }
     }

    public boolean removeRawPoly(String table, String id) {
        try(Connection connection = openDb()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + table + " WHERE _id = ?");
            preparedStatement.setString(1, id);
            return preparedStatement.executeUpdate() != 0;
        } catch (Exception e) {
            LOG.error("Failed to remove poly {} {}", table, id, e);
            return false;
        }
    }

    public void persistSupportPoly(String table, PolyRecord polyRecord) {
        try(Connection connection = openDb()) {

            String rawJSON = POLY_OBJECT_MAPPER.writeValueAsString(polyRecord);

            PreparedStatement dataStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE _id = ?;");
            dataStatement.setString(1, polyRecord._id());
            ResultSet dataResultSet = dataStatement.executeQuery();

            if (!dataResultSet.next()) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT OR REPLACE INTO " + table + "(_id, label,count, data) VALUES(?, ?, ?, ?);");
                preparedStatement.setString(1, polyRecord._id());
                preparedStatement.setString(2, polyRecord.label());
                preparedStatement.setLong(3, 1L);
                preparedStatement.setObject(4, rawJSON);
                preparedStatement.executeUpdate();
            } else {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + table + " SET _id = ?, label =?, count = count + 1, data =? WHERE id=?;");
                preparedStatement.setString(1, polyRecord._id());
                preparedStatement.setString(2, polyRecord.label());
                preparedStatement.setObject(3, rawJSON);
                preparedStatement.setObject(4, dataResultSet.getObject("id"));
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            LOG.error("Failed to import poly {} {}", table, polyRecord, e);
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


    /**
     * Evaluate statement and transform results to poly records
     * @param preparedStatement
     * @return
     * @throws PolyCoreException
     */
    private List<PolyRecord> evaluateStatement(PreparedStatement preparedStatement) throws PolyCoreException {

        List<PolyRecord> polyList = new ArrayList<>();
        try {
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                String rawJSON = resultSet.getString(PolyConstants.DATA_KEY);
                polyList.add(POLY_OBJECT_MAPPER.readValue(rawJSON, PolyRecord.class));
            }
            return polyList;
        } catch (Exception e) {
            LOG.warn("Failed to evaluate statement", e);
            throw new PolyCoreException(e);
        }
    }

    private List<PolyRecord> evaluateSupportStatement(PreparedStatement preparedStatement) throws PolyCoreException {

        List<PolyRecord> polyList = new ArrayList<>();
        try {
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                String rawJSON = resultSet.getString(PolyConstants.DATA_KEY);
                PolyRecord polyRecord = POLY_OBJECT_MAPPER.readValue(rawJSON, PolyRecord.class);
                polyRecord.count(resultSet.getObject("count"));
                polyList.add(polyRecord);
            }
            return polyList;
        } catch (Exception e) {
            LOG.warn("Failed to evaluate statement", e);
            throw new PolyCoreException(e);
        }
    }


}
