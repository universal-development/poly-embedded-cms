package com.unidev.polycms.hateoas;

import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polyembeddedcms.PolyCore;
import com.unidev.polyembeddedcms.PolyQuery;
import com.unidev.polyembeddedcms.SQLitePolyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static com.unidev.polycms.hateoas.HateoasPoly.hateoasPoly;
import static com.unidev.polycms.hateoas.HateoasPolyIndex.hateoasPolyIndex;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class StoragePageController {

    @Autowired
    private PolyCore polyCore;

    @Autowired
    private SQLitePolyService sqLitePolyService;

    @GetMapping(value = "/storage/{storage}/page/{page}", produces= MediaType.APPLICATION_JSON_VALUE)
    public List<HateoasPoly> index(@PathVariable("storage") String storage, @PathVariable("page") Long page) {
        PolyQuery polyQuery = PolyQuery.query().page(page);
        List<HateoasPoly> hateoasPolies = new ArrayList<>();
        sqLitePolyService.listNewPoly(polyQuery, storage).forEach( poly -> {
            hateoasPolies.add(hateoasPoly(poly));
        });

        return hateoasPolies;
    }

}
