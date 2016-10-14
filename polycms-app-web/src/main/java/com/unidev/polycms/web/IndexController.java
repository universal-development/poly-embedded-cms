package com.unidev.polycms.web;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Controller for index records
 */
@Controller
public class IndexController {

    @Autowired
    private WebPolyCore webPolyCore;

    @Autowired
    private HttpServletRequest httpServletRequest;

    // different mappings for bording web crawlers
    @RequestMapping(value = {"/",
            "/index.html", "/index.htm", "/index.htmlx",
            "/index.asp", "/index.aspx",
            "/index.php",
            "/index.cgi",
            "/index.rb",
            "/index.jsp", "/index.jsf", "/index.do", "/index.faces"

    })
    public String page(Model model) {

        WebPolyQuery polyQuery = WebPolyQuery.polyRequest()
                .model(model)
                .request(httpServletRequest)
                .page(0L);

        webPolyCore.addSupportModel(polyQuery).addNew(polyQuery, "/page/");
        model.addAttribute("view", "index");

        return "list";
    }

    @RequestMapping("/page/{page}")
    public String page(@PathVariable("page") Long page, Model model) {
        if (page == null || page <= 0L) {
            page = 1L;
        }
        WebPolyQuery polyQuery = WebPolyQuery.polyRequest()
                .model(model)
                .request(httpServletRequest)
                .page(page);
        webPolyCore.addSupportModel(polyQuery).addNew(polyQuery, "/page/");
        model.addAttribute("view", "index");
        return "list";
    }


    @RequestMapping(value = "/category/{category}")
    public String categoryPage(@PathVariable("category") String category, Model model) throws UnsupportedEncodingException {

        WebPolyQuery polyQuery = WebPolyQuery.polyRequest()
                .model(model)
                .request(httpServletRequest)
                .category(category)
                .page(0L);

        webPolyCore.addSupportModel(polyQuery).addNew(polyQuery, "/category/"+category);
        model.addAttribute("view", "category_index");
        return "list";
    }

    @RequestMapping(value = "/category/{category}/page/{page}")
    public String category(@PathVariable("category") String category, @PathVariable("page") Long page, Model model) throws UnsupportedEncodingException {
        WebPolyQuery polyQuery = WebPolyQuery.polyRequest()
                .model(model)
                .request(httpServletRequest)
                .category(category)
                .page(page);

        webPolyCore.addSupportModel(polyQuery).addNew(polyQuery, "/category/"+category);
        model.addAttribute("view", "category_index");
        return "list";
    }

    @RequestMapping(value = "/tags")
    public String tags(Model model) {
        WebPolyQuery polyQuery = WebPolyQuery.polyRequest()
                .model(model)
                .request(httpServletRequest)
                ;
        webPolyCore.addSupportModel(polyQuery);
        model.addAttribute("view", "tags");
        return "tags";
    }

    @RequestMapping(value = "/tag/{tag}")
    public String tag(@PathVariable("tag") String tag, Model model) throws UnsupportedEncodingException {
        WebPolyQuery polyQuery = WebPolyQuery.polyRequest()
                .model(model)
                .request(httpServletRequest)
                .tag(tag)
                .page(0L);

        webPolyCore.addSupportModel(polyQuery).addNew(polyQuery, "/tag/"+tag);
        model.addAttribute("view", "tags_index");
        return "list";
    }

    @RequestMapping(value = "/tag/{tag}/page/{page}")
    public String tag(@PathVariable("tag") String tag, @PathVariable("page") Long page, Model model) throws UnsupportedEncodingException {
        WebPolyQuery polyQuery = WebPolyQuery.polyRequest()
                .model(model)
                .request(httpServletRequest)
                .tag(tag)
                .page(page);

        webPolyCore.addSupportModel(polyQuery).addNew(polyQuery, "/tag/"+tag);
        model.addAttribute("view", "tags_index");
        return "list";
    }
}
