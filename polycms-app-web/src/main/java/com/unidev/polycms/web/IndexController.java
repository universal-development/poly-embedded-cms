package com.unidev.polycms.web;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Autowired
    private WebPolyCore webPolyCore;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping("/")
    public String page(Model model) {

        WebPolyQuery polyQuery = WebPolyQuery.polyRequest()
                .model(model)
                .request(httpServletRequest)
                .page(0L);

        webPolyCore.addSupportModel(polyQuery).addNew(polyQuery, "/page/");
        model.addAttribute("view", "index_list");


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
        model.addAttribute("view", "index_list");
        return "list";
    }

}
