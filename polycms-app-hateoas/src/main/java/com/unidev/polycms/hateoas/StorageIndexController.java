package com.unidev.polycms.hateoas;

import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polyembeddedcms.PolyCore;
import com.unidev.polyembeddedcms.PolyQuery;
import com.unidev.polyembeddedcms.PolyRecord;
import com.unidev.polyembeddedcms.SQLitePolyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static com.unidev.polycms.hateoas.HateoasPoly.hateoasPoly;
import static com.unidev.polycms.hateoas.HateoasPolyIndex.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Controller for handling storage requests
 */
@RestController
public class StorageIndexController {

    @Autowired
    private PolyCore polyCore;

    @Autowired
    private SQLitePolyService sqLitePolyService;

    @GetMapping(value = "/storage/{storage}/index.json", produces= MediaType.APPLICATION_JSON_VALUE)
    public HateoasPolyIndex index(@PathVariable("storage") String storage) {

        PolyQuery polyQuery = PolyQuery.query();

        Long totalPolys = sqLitePolyService.countPoly(polyQuery, storage);

        Long pages = totalPolys / PolyQuery.DEFAULT_ITEM_PER_PAGE;

        HateoasPolyIndex index = hateoasPolyIndex();

        LongStream.range(1, pages).forEach( page -> {
            BasicPoly pageRecord  = new BasicPoly()._id(page + "");
            HateoasPoly hateoasPoly = hateoasPoly(pageRecord);
            Link link = linkTo(StoragePageController.class).slash("storage").slash(storage).slash("page").slash(page).withSelfRel();
            hateoasPoly.add(link);
            index.add(hateoasPoly);
        });

        return index;
    }

}
