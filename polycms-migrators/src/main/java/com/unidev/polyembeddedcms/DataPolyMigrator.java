package com.unidev.polyembeddedcms;

import com.unidev.polydata.SQLitePolyMigrator;
import com.unidev.polydata.SQLiteStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Migrator for storing poly records
 */
public class DataPolyMigrator implements SQLitePolyMigrator {
    private static Logger LOG = LoggerFactory.getLogger(DataPolyMigrator.class);
    @Override
    public boolean canHandle(String poly) {
        return PolyConstants.CATEGORY_POLY.equalsIgnoreCase(poly);
    }

    @Override
    public void handle(String poly, Connection connection) throws SQLiteStorageException {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+poly+" (_id VARCHAR(255) PRIMARY KEY, label VARCHAR(255), category VARCHAR(255), tags VARCHAR(255), date NUMERIC,  data JSON)");
        } catch (SQLException e) {
            LOG.warn("Failed to run data migration ", e);
            throw new SQLiteStorageException(e);
        }
    }
}
