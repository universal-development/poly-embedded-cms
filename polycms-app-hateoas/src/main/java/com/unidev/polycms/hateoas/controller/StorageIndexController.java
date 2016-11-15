package com.unidev.polycms.hateoas.controller;

import com.unidev.polycms.hateoas.vo.HateoasPolyIndex;
import com.unidev.polyembeddedcms.PolyCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Controller for handling storage requests
 */
@RestController
public class StorageIndexController {

    private static Logger LOG = LoggerFactory.getLogger(StorageIndexController.class);

    @Autowired
    private PolyCore polyCore;

    @GetMapping(value = "/storage/{storage}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResourceSupport index(@PathVariable("storage") String storage) {
        if (!polyCore.existTenant(storage)) {
            LOG.warn("Not found storage {}", storage);
            throw new StorageNotFoundException("Storage " + storage + " not found");
        }
        HateoasPolyIndex hateoasPolyIndex = new HateoasPolyIndex();

        hateoasPolyIndex.add(
                linkTo(StorageIndexController.class).slash("categories").withRel("categories"),
                linkTo(StorageIndexController.class).slash("tags").withRel("tags")
        );

        return hateoasPolyIndex;
        //PolyQuery polyQuery = PolyQuery.query();

//        Long totalPolys = sqLitePolyService.countPoly(polyQuery, storage);
//
//        Long pages = totalPolys / PolyQuery.DEFAULT_ITEM_PER_PAGE;
//
//        HateoasPolyIndex index = hateoasPolyIndex();
//
//        LongStream.range(1, pages+1).forEach( page -> {
//            BasicPoly pageRecord  = new BasicPoly()._id(page + "");
//            HateoasPoly hateoasPoly = hateoasPoly(pageRecord);
//            Link link = linkTo(StoragePageController.class).slash("storage").slash(storage).slash("page").slash(page).withSelfRel();
//            hateoasPoly.add(link);
//            index.add(hateoasPoly);
//        });

        //return null;
    }

}
