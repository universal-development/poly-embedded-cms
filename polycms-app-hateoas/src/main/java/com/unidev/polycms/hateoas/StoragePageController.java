package com.unidev.polycms.hateoas;

import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polyembeddedcms.PolyQuery;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.stream.LongStream;

import static com.unidev.polycms.hateoas.HateoasPoly.hateoasPoly;
import static com.unidev.polycms.hateoas.HateoasPolyIndex.hateoasPolyIndex;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
public class StoragePageController {

    @GetMapping(value = "/storage/{storage}/page/{page}", produces= MediaType.APPLICATION_JSON_VALUE)
    public HateoasPolyIndex index(@PathVariable("storage") String storage, @PathVariable("page") Long page) {

        PolyQuery polyQuery = PolyQuery.query().page(page);


        return null;
    }

}
