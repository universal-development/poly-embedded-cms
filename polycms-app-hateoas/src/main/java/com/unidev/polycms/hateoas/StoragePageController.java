package com.unidev.polycms.hateoas;

import com.unidev.polycms.hateoas.vo.HateoasPolyIndex;
import com.unidev.polyembeddedcms.PolyCore;
import com.unidev.polyembeddedcms.PolyQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoragePageController {

    @Autowired
    private PolyCore polyCore;



    @GetMapping(value = "/storage/{storage}/page/{page}", produces= MediaType.APPLICATION_JSON_VALUE)
    public HateoasPolyIndex index(@PathVariable("storage") String storage, @PathVariable("page") Long page) {
        PolyQuery polyQuery = PolyQuery.query().page(page);
        HateoasPolyIndex hateoasPolyIndex = new HateoasPolyIndex();
//        sqLitePolyService.listNewPoly(polyQuery, storage).forEach( poly -> {
//            hateoasPolyIndex.add(hateoasPoly(poly));
//        });

        return hateoasPolyIndex;
    }

}
