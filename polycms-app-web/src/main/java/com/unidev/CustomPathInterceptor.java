package com.unidev;

import com.unidev.platform.j2ee.common.WebUtils;
import com.unidev.polycms.web.WebPolyCore;
import com.unidev.polycms.web.WebPolyQuery;
import com.unidev.polyembeddedcms.PolyRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Request handler to match requests on custom paths
 */
@ControllerAdvice
public class CustomPathInterceptor {

    private static Logger LOG = LoggerFactory.getLogger(CustomPathInterceptor.class);

    @Autowired
    @Lazy
    private WebUtils webUtils;

    @Autowired
    @Lazy
    private WebPolyCore webPolyCore;

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleCustomPath(HttpServletRequest request, Exception e, Model model)   {
        LOG.debug("handleCustomPath {}", request.getRequestURI());
        WebPolyQuery polyQuery = WebPolyQuery.polyRequest()
                .model(model)
                .request(request)
                .polyId(request.getRequestURI().substring(1))
                ;
        String template = webPolyCore.fetchTemplateRoot(polyQuery);
        Optional<PolyRecord> polyRecord = webPolyCore.fetchPoly(polyQuery);
        if (polyRecord.isPresent()) {
            webPolyCore.addSupportModel(polyQuery).addPoly(polyQuery, polyRecord.get());
            model.addAttribute("view", "poly");
            return template + "/poly";
        }
        return template + "/404";

    }

    @ExceptionHandler
    public String errorHandler(HttpServletRequest request, Exception e, Model model)   {
        LOG.debug("errorHandler {}", request.getRequestURI());
        WebPolyQuery polyQuery = WebPolyQuery.polyRequest()
                .model(model)
                .request(request)
                .polyId(request.getRequestURI().substring(1))
                ;
        String template = webPolyCore.fetchTemplateRoot(polyQuery);
        return template + "/error";

    }


}
