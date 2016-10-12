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
        String tenant = fetchTenant(polyRequest.request());
        List<BasicPoly> categories = sqLitePolyService.fetchCategories(tenant);
        polyRequest.model().addAttribute("categories", categories);
        return this;
    }

    public WebPolyCore addTags(WebPolyQuery polyRequest) {
        String tenant = fetchTenant(polyRequest.request());
        List<BasicPoly> tags = sqLitePolyService.fetchTags(tenant);

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
        String tenant = fetchTenant(polyRequest.request());
        List<BasicPoly> polyList = sqLitePolyService.listNewPoly(polyRequest, tenant);
        return addPagination(polyRequest, urlBegin, polyList);
    }

    public WebPolyCore addPoly(WebPolyQuery polyQuery) {
        String tenant = fetchTenant(polyQuery.request());
        PolyRecord polyRecord = sqLitePolyService.fetchPoly(polyQuery.polyId(), tenant);
        polyQuery.model().addAttribute("poly", polyRecord);

        return this;
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
     * Fetch tenant root file
     * @param httpServletRequest
     * @return
     */
    private String fetchTenant(HttpServletRequest httpServletRequest) {
        String domain = webUtils.getDomain(httpServletRequest);
        return domain;
    }



}
