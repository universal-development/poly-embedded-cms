package com.unidev;

import com.unidev.polycms.web.ListNewPolyQuery;
import com.unidev.polycms.web.WebPolyCore;
import com.unidev.polydata.SQLiteStorage;
import com.unidev.polydata.SQLiteStorageException;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polydata.domain.Poly;
import com.unidev.polyembeddedcms.*;
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
import java.util.Date;
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
	public void testSinglePolyLoading() throws SQLiteStorageException {
		SQLiteStorage sqLiteStorage = new SQLiteStorage(dbFile.getAbsolutePath());
		sqLiteStorage.setPolyMigrators(Arrays.asList(new DataPolyMigrator()));

		String postId = "post_1";

		PolyRecord data = new PolyRecord()._id(postId).label("Tomato").category("Test").tags("tag1, tag2").date(new Date());
		sqLiteStorage.save(PolyConstants.DATA_POLY, data);

		HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.doReturn(new StringBuffer("http://" + domain)).when(httpServletRequest).getRequestURL();

		PolyRecord poly = webPolyCore.fetchPoly(postId, httpServletRequest);

		assertThat(poly, is(not(nullValue())));
		assertThat(poly._id(), is(postId));
		assertThat(poly.date(), is(notNullValue()));

		Mockito.doReturn(new StringBuffer("http://" + domain)).when(httpServletRequest).getRequestURL();
		PolyRecord notExistingPoly = webPolyCore.fetchPoly("not-existing-id", httpServletRequest);
		assertThat(notExistingPoly, is(nullValue()));

	}

	@Test
	public void testPolyPagination() throws SQLiteStorageException {
		SQLiteStorage sqLiteStorage = new SQLiteStorage(dbFile.getAbsolutePath());
		sqLiteStorage.setPolyMigrators(Arrays.asList(new DataPolyMigrator()));

		for(int i = 0;i<10;i++) {
			String postId = "post_" + i;

			PolyRecord data = new PolyRecord()._id(postId).label("Label " + i ).category("Cat1").tags("tag1, tag2").date(new Date( System.currentTimeMillis() + 100 * i));
			sqLiteStorage.save(PolyConstants.DATA_POLY, data);
		}

		HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.doReturn(new StringBuffer("http://" + domain)).when(httpServletRequest).getRequestURL();

		ListNewPolyQuery listNewPolyQuery = ListNewPolyQuery.query().page(0L).itemPerPage(5);

		List<BasicPoly> basicPolyList = webPolyCore.listNewPoly(listNewPolyQuery, httpServletRequest);
		assertThat(basicPolyList, is(notNullValue()));
		assertThat(basicPolyList.size(), is(5));

	}

}
