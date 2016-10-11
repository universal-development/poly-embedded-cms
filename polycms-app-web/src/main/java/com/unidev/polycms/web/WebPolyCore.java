package com.unidev.polycms.web;

import com.unidev.platform.j2ee.common.WebUtils;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polyembeddedcms.PolyQuery;
import com.unidev.polyembeddedcms.PolyRecord;
import com.unidev.polyembeddedcms.SQLitePolyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Core service for web app
 */
@Service
public class WebPolyCore {

    private static Logger LOG = LoggerFactory.getLogger(WebPolyCore.class);


    private SQLitePolyService sqLitePolyService;

    private WebUtils webUtils;


    public WebPolyCore( @Autowired SQLitePolyService sqLitePolyService, @Autowired WebUtils webUtils) {
        this.sqLitePolyService = sqLitePolyService;
        this.webUtils = webUtils;
    }

    /**
     * List polys filtered by page, category, tag and ordered by date
     * @param httpServletRequest
     * @return
     */
    public List<BasicPoly> listNewPoly(PolyQuery listNewPolyQuery, HttpServletRequest httpServletRequest) {
        String tenant = fetchTenant(httpServletRequest);
        return sqLitePolyService.listNewPoly(listNewPolyQuery, tenant);
    }

    /**
     * Fetch categories records
     * @param httpServletRequest
     * @return
     */
    public List<BasicPoly> fetchCategories(HttpServletRequest httpServletRequest) {
        String tenant = fetchTenant(httpServletRequest);
        return sqLitePolyService.fetchCategories(tenant);
    }

    /**
     * Fetch tags from poly store
     * @param httpServletRequest
     * @return
     */
    public List<BasicPoly> fetchTags(HttpServletRequest httpServletRequest) {
        String tenant = fetchTenant(httpServletRequest);
        return sqLitePolyService.fetchTags(tenant);
    }

    /**
     * Fetch poly by ID
     * @param id
     * @param httpServletRequest
     * @return poly instance or null if poly is not available
     */
    public PolyRecord fetchPoly(String id, HttpServletRequest httpServletRequest) {
        String tenant = fetchTenant(httpServletRequest);
        return sqLitePolyService.fetchPoly(id, tenant);
    }

    /**
     * Fetch tenant root file
     * @param httpServletRequest
     * @return
     */
    public String fetchTenant(HttpServletRequest httpServletRequest) {
        String domain = webUtils.getDomain(httpServletRequest);
        return domain;
    }



}
