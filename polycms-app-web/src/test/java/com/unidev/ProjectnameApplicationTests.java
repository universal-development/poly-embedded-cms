package com.unidev;

import com.unidev.polycms.web.WebPolyCore;
import com.unidev.polydata.SQLiteStorage;
import com.unidev.polydata.SQLiteStorageException;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polyembeddedcms.PolyConstants;
import com.unidev.polyembeddedcms.PolyCore;
import com.unidev.polyembeddedcms.PolyRecord;
import com.unidev.polyembeddedcms.TagsPolyMigrator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectnameApplicationTests {

	@Autowired
	private WebPolyCore webPolyCore;

	@Autowired
	private PolyCore polyCore;

	private File root;
	private String domain = "test.com";
	private File dbFile;

	@Before
	public void init() {
		root = polyCore.fetchStorageRoot(domain);
		root.mkdirs();

		dbFile = new File(root, PolyConstants.DB_FILE);
		dbFile.deleteOnExit();
	}

	@Test
	public void contextLoads() {

	}

	@Test
	public void testTagsListing() throws MalformedURLException, SQLiteStorageException {
		SQLiteStorage sqLiteStorage = new SQLiteStorage(dbFile.getAbsolutePath());
		sqLiteStorage.setPolyMigrators(Arrays.asList(new TagsPolyMigrator()));

		PolyRecord tagTomato = new PolyRecord()._id("tomato").label("Tomato").count(100);
		sqLiteStorage.save(PolyConstants.TAGS_POLY, tagTomato);

		PolyRecord tagPotato = new PolyRecord()._id("potato").label("Potato").count(10);
		sqLiteStorage.save(PolyConstants.TAGS_POLY, tagPotato);


		HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.doReturn(new StringBuffer("http://" + domain)).when(httpServletRequest).getRequestURL();

		List<BasicPoly> list = webPolyCore.fetchTags(httpServletRequest);
		assertThat(list, is(not(nullValue())));
		assertThat(list.size(), is(2));

		BasicPoly first = list.get(0);

		assertThat(first.get(PolyConstants.COUNT_KEY), is(100));
		assertThat(first._id(), is("tomato"));
	}

	@Test
	public void testPolyLoading() {

	}

}
