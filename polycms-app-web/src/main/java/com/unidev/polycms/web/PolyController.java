package com.unidev.polycms.web;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for poly records fetching
 */
@Controller
public class PolyController {

    @Autowired
    private WebPolyCore webPolyCore;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping("/poly/{polyId:.*}")
    public String page(@PathVariable String polyId, Model model) {

        WebPolyQuery polyQuery = WebPolyQuery.polyRequest()
                .model(model)
                .request(httpServletRequest)
                .polyId(polyId)
                ;
        webPolyCore.addSupportModel(polyQuery).addPoly(polyQuery);
        model.addAttribute("view", "poly");
        String template = webPolyCore.fetchTemplateRoot(polyQuery);
        return template + "/poly";
    }
}
