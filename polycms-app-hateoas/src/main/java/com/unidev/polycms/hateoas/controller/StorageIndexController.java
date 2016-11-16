/**
 * Copyright (c) 2016 Denis O <denis@universal-development.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unidev.polycms.hateoas.controller;

import com.unidev.polycms.hateoas.vo.HateoasResponse;
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
        HateoasResponse hateoasPolyIndex = new HateoasResponse();

        hateoasPolyIndex.add(
                linkTo(StorageIndexController.class).slash("storage").slash(storage).slash("categories").withRel("categories"),
                linkTo(StorageIndexController.class).slash("storage").slash(storage).slash("tags").withRel("tags"),
                linkTo(StorageQueryController.class).slash("storage").slash(storage).slash("query").withRel("query")
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
        return new HateoasResponse().data(polyCore.fetchSqliteStorage(storage).listCategories());
    }

    @GetMapping(value = "/storage/{storage}/tags", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResourceSupport tags(@PathVariable("storage") String storage) {
        if (!polyCore.existTenant(storage)) {
            LOG.warn("Not found storage {}", storage);
            throw new StorageNotFoundException("Storage " + storage + " not found");
        }
        return new HateoasResponse().data(polyCore.fetchSqliteStorage(storage).listTags());
    }

}
