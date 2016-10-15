package com.unidev;

import com.unidev.platform.j2ee.common.WebUtils;
import com.unidev.polycms.web.WebPolyCore;
import com.unidev.polycms.web.WebPolyQuery;
import com.unidev.polyembeddedcms.PolyRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Request handler to match requests for specific poly
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {

    private static Logger LOG = LoggerFactory.getLogger(RequestInterceptor.class);

    @Autowired
    @Lazy
    private WebUtils webUtils;

    @Autowired
    @Lazy
    private WebPolyCore webPolyCore;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        WebPolyQuery webPolyQuery = WebPolyQuery.polyRequest();
        webPolyQuery.setRequest(request);
        webPolyQuery.setPolyId(request.getRequestURI());

        PolyRecord polyRecord = webPolyCore.fetchPoly(webPolyQuery);
        LOG.info("Polyrecord {} for {}", polyRecord, request.getRequestURI());
        if (polyRecord != null) {
            request.getRequestDispatcher("/poly/" + polyRecord._id()).forward(request, response);
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
