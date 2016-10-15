package com.unidev.polycms.hateoas;


import com.unidev.polyembeddedcms.PolyRecord;
import org.springframework.hateoas.ResourceSupport;

public class HateoasPoly extends ResourceSupport {

    private PolyRecord polyRecord;

    public static HateoasPoly hateoasPoly(PolyRecord polyRecord) {
        return new HateoasPoly().polyRecord(polyRecord);
    }

    public HateoasPoly polyRecord(PolyRecord polyRecord) {
        this.polyRecord = polyRecord;
        return this;
    }

    public PolyRecord polyRecord() {
        return polyRecord;
    }

}
