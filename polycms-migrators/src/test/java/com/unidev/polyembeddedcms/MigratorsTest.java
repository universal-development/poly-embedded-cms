package com.unidev.polyembeddedcms;


import com.unidev.polydata.SQLiteStorage;
import com.unidev.polydata.SQLiteStorageException;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polydata.domain.Poly;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MigratorsTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private SQLiteStorage sqLiteStorage;

    @Before
    public void init() {
        File dbFile = new File(temporaryFolder.getRoot(), "poly.db");
        sqLiteStorage = new SQLiteStorage(dbFile.getAbsolutePath());
    }

    @Test
    public void tagsMigrator() throws SQLiteStorageException, SQLException {
        sqLiteStorage.setPolyMigrators(Arrays.asList(new TagsPolyMigrator()));

        PolyRecord polyRecord = new PolyRecord()._id("tomato").label("Tomato");
        sqLiteStorage.save(PolyConstants.TAGS_POLY, polyRecord);


        PreparedStatement preparedStatement = sqLiteStorage.openDb().prepareStatement("SELECT count(*) as totals FROM " + PolyConstants.TAGS_POLY);
        List<BasicPoly> polyList = sqLiteStorage.evaluateStatement(preparedStatement);

        assertThat(polyList.size(), is(1));
        assertThat(polyList.get(0).get("totals"), is(1));

    }

}
