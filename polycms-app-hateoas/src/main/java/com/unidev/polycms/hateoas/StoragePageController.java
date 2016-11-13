package com.unidev.polycms.hateoas;

import com.unidev.polyembeddedcms.PolyCore;
import com.unidev.polyembeddedcms.PolyQuery;
import com.unidev.polyembeddedcms.sqlite.SQLitePolyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.unidev.polycms.hateoas.HateoasPoly.hateoasPoly;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class StoragePageController {

    @Autowired
    private PolyCore polyCore;

    @Autowired
    private SQLitePolyService sqLitePolyService;

    @GetMapping(value = "/storage/{storage}/page/{page}", produces= MediaType.APPLICATION_JSON_VALUE)
    public HateoasPolyIndex index(@PathVariable("storage") String storage, @PathVariable("page") Long page) {
        PolyQuery polyQuery = PolyQuery.query().page(page);
        HateoasPolyIndex hateoasPolyIndex = new HateoasPolyIndex();
        sqLitePolyService.listNewPoly(polyQuery, storage).forEach( poly -> {
            hateoasPolyIndex.add(hateoasPoly(poly));
        });

        return hateoasPolyIndex;
    }

}
