package com.unidev.polycms.web;

import com.unidev.platform.j2ee.common.WebUtils;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polyembeddedcms.PolyCore;
import com.unidev.polyembeddedcms.PolyQuery;
import com.unidev.polyembeddedcms.PolyRecord;
import com.unidev.polyembeddedcms.SQLitePolyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Core service for web app
 */
@Service
public class WebPolyCore {
    public static final int ITEM_PER_PAGE = 40;
    private static Logger LOG = LoggerFactory.getLogger(WebPolyCore.class);

    private SQLitePolyService sqLitePolyService;

    private WebUtils webUtils;

    private PolyCore polyCore;


    public WebPolyCore addCategories(WebPolyQuery polyRequest) {
        List<BasicPoly> categories = fetchCategories(polyRequest.request());
        polyRequest.model().addAttribute("categories", categories);
        return this;
    }

    public WebPolyCore addTags(WebPolyQuery polyRequest) {
        List<BasicPoly> tags = fetchCategories(polyRequest.request());
        polyRequest.model().addAttribute("tags", tags);
        return this;
    }

    /**
     * Add support objects to model
     * @param polyRequest
     * @return
     */
    public WebPolyCore addSupportModel(WebPolyQuery polyRequest) {
        return addCategories(polyRequest).addTags(polyRequest);
    }

    public WebPolyCore addNew(WebPolyQuery polyRequest, String urlBegin) {
        List<BasicPoly> polyList = listNewPoly(polyRequest, polyRequest.request());
        return addPagination(polyRequest, urlBegin, polyList);
    }

    public WebPolyCore addPagination(WebPolyQuery polyRequest, String urlBegin, List<BasicPoly> items) {
        polyRequest.model().addAttribute("page", polyRequest.page());
        polyRequest.model().addAttribute("items", items);

        Long backPage = polyRequest.page() - 1;
        Long nextPage = polyRequest.page() + 1;

        if (backPage <= 0) {
            polyRequest.model().addAttribute("backPage", null);
        } else {
            polyRequest.model().addAttribute("backPage", urlBegin + backPage);
        }

        if (items != null && items.size() > 0) {
            polyRequest.model().addAttribute("nextPage", urlBegin + nextPage);
        } else {
            polyRequest.model().addAttribute("nextPage", null);
        }

        List<Map<String, String>> pages = new ArrayList<>();
        for(Long pageId = polyRequest.page() - WebPolyCore.ITEM_PER_PAGE / 2;pageId<= polyRequest.page() + WebPolyCore.ITEM_PER_PAGE / 2;pageId++) {
            if (pageId >= 1) {
                Map<String, String> pageData = new HashMap<>();
                pageData.put("page",urlBegin + pageId);
                pageData.put("pageId", pageId + "");
                pages.add(pageData);
            }
        }
        polyRequest.model().addAttribute("pages", pages);
        return this;
    }


    public WebPolyCore( @Autowired SQLitePolyService sqLitePolyService, @Autowired WebUtils webUtils, @Autowired PolyCore polyCore) {
        this.sqLitePolyService = sqLitePolyService;
        this.webUtils = webUtils;
        this.polyCore = polyCore;
    }

    /**
     * List polys filtered by page, category, tag and ordered by date
     * @param httpServletRequest
     * @return
     */
    private List<BasicPoly> listNewPoly(PolyQuery listNewPolyQuery, HttpServletRequest httpServletRequest) {
        String tenant = fetchTenant(httpServletRequest);
        return sqLitePolyService.listNewPoly(listNewPolyQuery, tenant);
    }

    /**
     * Fetch categories records
     * @param httpServletRequest
     * @return
     */
    private List<BasicPoly> fetchCategories(HttpServletRequest httpServletRequest) {
        String tenant = fetchTenant(httpServletRequest);
        return sqLitePolyService.fetchCategories(tenant);
    }

    /**
     * Fetch tags from poly store
     * @param httpServletRequest
     * @return
     */
    private List<BasicPoly> fetchTags(HttpServletRequest httpServletRequest) {
        String tenant = fetchTenant(httpServletRequest);
        return sqLitePolyService.fetchTags(tenant);
    }

    /**
     * Fetch poly by ID
     * @param id
     * @param httpServletRequest
     * @return poly instance or null if poly is not available
     */
    private PolyRecord fetchPoly(String id, HttpServletRequest httpServletRequest) {
        String tenant = fetchTenant(httpServletRequest);
        return sqLitePolyService.fetchPoly(id, tenant);
    }

    /**
     * Fetch tenant root file
     * @param httpServletRequest
     * @return
     */
    private String fetchTenant(HttpServletRequest httpServletRequest) {
        String domain = webUtils.getDomain(httpServletRequest);
        return domain;
    }



}
