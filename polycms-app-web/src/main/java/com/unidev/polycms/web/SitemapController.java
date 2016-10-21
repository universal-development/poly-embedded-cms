package com.unidev.polycms.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class SitemapController {

    @RequestMapping(value = "/sitemapindex.xsl",method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemapIndex() {
        return "sitemapindex.xsl";
    }

    @RequestMapping(value = "/sitemap.xsl",method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemap() throws IOException {
        return "sitemap.xsl";
    }

}
