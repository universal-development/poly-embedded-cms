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
import com.unidev.polyembeddedcms.PolyConstants;
import com.unidev.polyembeddedcms.PolyCore;
import com.unidev.polyembeddedcms.PolyQuery;
import com.unidev.polyembeddedcms.PolyRecord;
import com.unidev.polyembeddedcms.sqlite.SQLitePolyStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import static com.unidev.polycms.hateoas.vo.HateoasResponse.hateoasResponse;

/**
 * Controller for handling storage queries
 */
@RestController
public class StorageQueryController {

    private static Logger LOG = LoggerFactory.getLogger(StorageQueryController.class);

    @Autowired
    private PolyCore polyCore;

    @PostMapping(value = "/storage/{storage}/query", produces= MediaType.APPLICATION_JSON_VALUE)
    public HateoasResponse query(@PathVariable("storage") String storage, @RequestBody(required = false) PolyQuery polyQuery) {
        if (!polyCore.existTenant(storage)) {
            LOG.warn("Not found storage {}", storage);
            throw new StorageNotFoundException("Storage " + storage + " not found");
        }
        if (polyQuery != null) {
            if (polyQuery.getItemPerPage() > 256) {
                polyQuery.itemPerPage(PolyConstants.DEFAULT_ITEM_PER_PAGE);
            }
            if (polyQuery.page() < 0) {
                polyQuery.page(0L);
            }
        } else {
            polyQuery = new PolyQuery();
        }

        SQLitePolyStorage sqLitePolyStorage = polyCore.fetchSqliteStorage(storage);
        List<PolyRecord> polyRecords = sqLitePolyStorage.listPoly(polyQuery);
        return hateoasResponse().data(polyRecords);
    }

    @GetMapping(value = "/storage/{storage}/poly/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public HateoasResponse fetchPoly(@PathVariable("storage") String storage, @PathVariable("id") String id) {
        if (!polyCore.existTenant(storage)) {
            LOG.warn("Not found storage {}", storage);
            throw new StorageNotFoundException("Storage " + storage + " not found");
        }
        SQLitePolyStorage sqLitePolyStorage = polyCore.fetchSqliteStorage(storage);

        Optional<PolyRecord> polyRecord = sqLitePolyStorage.fetchPoly(id);
        polyRecord.orElseThrow(StorageNotFoundException::new);
        HateoasResponse hateoasResponse = hateoasResponse().data(polyRecord.get());
        hateoasResponse.add(
                linkTo(StorageIndexController.class).slash("storage").slash(storage).slash("poly").slash(id).withSelfRel()
        );
        return hateoasResponse;
    }

}
