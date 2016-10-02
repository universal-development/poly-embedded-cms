package com.unidev.polyembeddedcms;

import com.unidev.polydata.SQLitePolyMigrator;
import com.unidev.polydata.SQLiteStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default SQLite poly migrator, create poly table with (_id, label, json)
 *
 */
public class JSONColumnPolyMigrator implements SQLitePolyMigrator {

    private static Logger LOG = LoggerFactory.getLogger(JSONColumnPolyMigrator.class);

    public static List<SQLitePolyMigrator> sqLitePolyMigratorList() {
        return Stream.of(new JSONColumnPolyMigrator()).collect(Collectors.toList());
    }

    @Override
    public boolean canHandle(String poly) {
        LOG.info("Default handling for any poly {}", poly);
        return true;
    }

    @Override
    public void handle(String poly, Connection connection) throws SQLiteStorageException {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+poly+" (_id VARCHAR(255) PRIMARY KEY, label VARCHAR(255), data JSON)");
        } catch (SQLException e) {
            LOG.warn("Failed to run migration", e);
            throw new SQLiteStorageException(e);
        }
    }
}
