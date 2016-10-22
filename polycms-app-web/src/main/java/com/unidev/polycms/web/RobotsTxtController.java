package com.unidev.polycms.web;

import com.unidev.platform.j2ee.common.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = {"/robots.txt"})
public class RobotsTxtController {

    @Autowired
    private WebUtils webUtils;

    @Autowired
    private HttpServletRequest context;

    @GetMapping
    public String buildRobotsTxt(Model model, HttpServletResponse response) {
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        String domain = webUtils.removeWWW(webUtils.getDomain(context));
        model.addAttribute("schema", context.getScheme());
        model.addAttribute("domain", domain);
        return "robots";
    }

}
