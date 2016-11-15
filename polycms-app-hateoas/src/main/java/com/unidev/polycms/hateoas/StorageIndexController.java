package com.unidev.polycms.hateoas;

import com.unidev.polycms.hateoas.vo.HateoasPoly;
import com.unidev.polycms.hateoas.vo.HateoasPolyIndex;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polyembeddedcms.PolyCore;
import com.unidev.polyembeddedcms.PolyQuery;
import com.unidev.polyembeddedcms.sqlite.SQLitePolyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.util.stream.LongStream;

import static com.unidev.polycms.hateoas.vo.HateoasPoly.hateoasPoly;
import static com.unidev.polycms.hateoas.vo.HateoasPolyIndex.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Controller for handling storage requests
 */
@RestController
public class StorageIndexController {

    private static Logger LOG = LoggerFactory.getLogger(StorageIndexController.class);


    @Autowired
    private PolyCore polyCore;

    @Autowired
    private SQLitePolyService sqLitePolyService;

    @GetMapping(value = "/storage/{storage}", produces= MediaType.APPLICATION_JSON_VALUE)
    public HateoasPolyIndex index(@PathVariable("storage") String storage) {

        PolyQuery polyQuery = PolyQuery.query();

        Long totalPolys = sqLitePolyService.countPoly(polyQuery, storage);

        Long pages = totalPolys / PolyQuery.DEFAULT_ITEM_PER_PAGE;

        HateoasPolyIndex index = hateoasPolyIndex();

        LongStream.range(1, pages+1).forEach( page -> {
            BasicPoly pageRecord  = new BasicPoly()._id(page + "");
            HateoasPoly hateoasPoly = hateoasPoly(pageRecord);
            Link link = linkTo(StoragePageController.class).slash("storage").slash(storage).slash("page").slash(page).withSelfRel();
            hateoasPoly.add(link);
            index.add(hateoasPoly);
        });

        return index;
    }

}
