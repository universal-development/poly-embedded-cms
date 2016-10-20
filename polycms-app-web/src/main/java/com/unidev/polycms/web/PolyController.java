package com.unidev.polycms.web;

import com.unidev.polyembeddedcms.PolyRecord;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

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
        String template = webPolyCore.fetchTemplateRoot(polyQuery);
        Optional<PolyRecord> polyRecord = webPolyCore.fetchPoly(polyQuery);
        webPolyCore.addSupportModel(polyQuery).addPoly(polyQuery);
        model.addAttribute("view", "poly");
        if (polyRecord.isPresent()) {
            webPolyCore.addSupportModel(polyQuery).addPoly(polyQuery, polyRecord.get());
            model.addAttribute("view", "poly");
            return template + "/poly";
        }
        return template + "/404";
    }
}
