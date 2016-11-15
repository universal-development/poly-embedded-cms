package com.unidev.polycms.hateoas.controller;

import com.unidev.polycms.hateoas.vo.HateoasPolyIndex;
import com.unidev.polydata.domain.Poly;
import com.unidev.polyembeddedcms.PolyCore;
import com.unidev.polyembeddedcms.PolyRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
                linkTo(StorageIndexController.class).slash("storage").slash(storage).slash("categories").withRel("categories"),
                linkTo(StorageIndexController.class).slash("storage").slash(storage).slash("tags").withRel("tags")
        );
        hateoasPolyIndex.data(storage);
        return hateoasPolyIndex;
    }

    @GetMapping(value = "/storage/{storage}/categories", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResourceSupport categories(@PathVariable("storage") String storage) {
        if (!polyCore.existTenant(storage)) {
            LOG.warn("Not found storage {}", storage);
            throw new StorageNotFoundException("Storage " + storage + " not found");
        }
        return new HateoasPolyIndex().data(polyCore.fetchSqliteStorage(storage).listCategories());
    }

    @GetMapping(value = "/storage/{storage}/tags", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResourceSupport tags(@PathVariable("storage") String storage) {
        if (!polyCore.existTenant(storage)) {
            LOG.warn("Not found storage {}", storage);
            throw new StorageNotFoundException("Storage " + storage + " not found");
        }
        return new HateoasPolyIndex().data(polyCore.fetchSqliteStorage(storage).listTags());
    }

}
