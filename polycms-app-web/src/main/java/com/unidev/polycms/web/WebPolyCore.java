package com.unidev.polycms.web;

import com.unidev.platform.j2ee.common.WebUtils;
import com.unidev.polydata.FlatFileStorage;
import com.unidev.polydata.FlatFileStorageMapper;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polydata.domain.Poly;
import com.unidev.polyembeddedcms.PolyConstants;
import com.unidev.polyembeddedcms.PolyCore;
import com.unidev.polyembeddedcms.PolyRecord;
import com.unidev.polyembeddedcms.SQLitePolyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

import static com.unidev.polydata.FlatFileStorageMapper.*;

/**
 * Core service for web app
 */
@Service
public class WebPolyCore {
    public static final int ITEM_PER_PAGE = 40;
    public static final String CATEGORIES_KEY = "categories";
    public static final String TAGS_KEY = "tags";
    public static final String POLY_KEY = "poly";
    public static final String ITEMS_KEY = "items";
    public static final String POLY_REQUEST_KEY = "polyRequest";

    public static final String TEMPLATE_KEY = "template";

    private static Logger LOG = LoggerFactory.getLogger(WebPolyCore.class);

    private SQLitePolyService sqLitePolyService;

    private WebUtils webUtils;

    private PolyCore polyCore;


    public String fetchTemplateRoot(WebPolyQuery polyRequest) {
        Optional<FlatFileStorage> flatFileStorage = fetchTenantFlatFileStorage(polyRequest.request());
        if (!flatFileStorage.isPresent()) {
            return "";
        }
        return flatFileStorage.get().metadata().fetch(TEMPLATE_KEY, "");
    }

    public WebPolyCore addCategories(WebPolyQuery polyRequest) {
        String tenant = fetchTenant(polyRequest.request());
        List<BasicPoly> categories = sqLitePolyService.fetchCategories(tenant);
        polyRequest.model().addAttribute(CATEGORIES_KEY, categories);
        return this;
    }

    public WebPolyCore addTags(WebPolyQuery polyRequest) {
        String tenant = fetchTenant(polyRequest.request());
        List<BasicPoly> tags = sqLitePolyService.fetchTags(tenant);

        polyRequest.model().addAttribute(TAGS_KEY, tags);
        return this;
    }

    /**
     * Add support objects to model
     * @param polyRequest
     * @return
     */
    public WebPolyCore addSupportModel(WebPolyQuery polyRequest) {
        polyRequest.model().addAttribute(POLY_REQUEST_KEY, polyRequest); // link to own request, recursion(?)
        return addCategories(polyRequest).addTags(polyRequest);
    }

    public WebPolyCore addNew(WebPolyQuery polyRequest, String urlBegin) {
        String tenant = fetchTenant(polyRequest.request());
        List<BasicPoly> polyList = sqLitePolyService.listNewPoly(polyRequest, tenant);
        return addPagination(polyRequest, urlBegin, polyList);
    }

    /**
     * Check if exists poly or not
     * @param polyQuery
     * @return
     */
    public Optional<PolyRecord> fetchPoly(WebPolyQuery polyQuery) {
        String tenant = fetchTenant(polyQuery.request());
        return sqLitePolyService.fetchPoly(polyQuery.polyId(), tenant);
    }

    public Long fetchPolyCount(WebPolyQuery polyQuery) {
        String tenant = fetchTenant(polyQuery.request());
        return sqLitePolyService.countPoly(polyQuery, tenant);
    }

    public WebPolyCore addPoly(WebPolyQuery polyQuery) {
        String tenant = fetchTenant(polyQuery.request());
        Optional<PolyRecord> polyRecord = sqLitePolyService.fetchPoly(polyQuery.polyId(), tenant);

        if (polyRecord.isPresent()) {
            polyQuery.model().addAttribute(POLY_KEY, polyRecord.get());
        } else {
            LOG.warn("Poly {} not found for tenant", polyQuery.polyId(), tenant);
        }

        return this;
    }

    public WebPolyCore addPoly(WebPolyQuery polyQuery, Poly poly) {
        polyQuery.model().addAttribute(POLY_KEY, poly);
        return this;
    }

    public WebPolyCore addPagination(WebPolyQuery polyRequest, String urlBegin, List<BasicPoly> items) {
        polyRequest.model().addAttribute("page", polyRequest.page());
        polyRequest.model().addAttribute(ITEMS_KEY, items);

        Long backPage = polyRequest.page() - 1;
        Long nextPage = polyRequest.page() + 1;

        if (backPage <= 0) {
            polyRequest.model().addAttribute("backPage", null);
        } else {
            polyRequest.model().addAttribute("backPage", urlBegin + backPage);
        }

        boolean listIsEmpty = false;
        if (items != null && items.size() > 0) {
            polyRequest.model().addAttribute("nextPage", urlBegin + nextPage);
        } else {
            listIsEmpty = true;
            polyRequest.model().addAttribute("nextPage", null);
        }

        List<Map<String, String>> pages = new ArrayList<>();
        for(Long pageId = polyRequest.page() - WebPolyCore.ITEM_PER_PAGE / 2;pageId<= polyRequest.page() + WebPolyCore.ITEM_PER_PAGE / 2;pageId++) {
            //TODO: somehow limit empty "next" pages
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


    public Optional<FlatFileStorage> fetchTenantFlatFileStorage(HttpServletRequest httpServletRequest) {
        String tenant = fetchTenant(httpServletRequest);
        File tenantRoot = polyCore.fetchStorageRoot(tenant);
        File flatFile = new File(tenantRoot, PolyConstants.FLAT_FILE_DB);
        if (!flatFile.exists()) {
            return Optional.empty();
        }
        return Optional.of(storageMapper().loadSource(flatFile).load());
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
