package com.unidev.polycms.hateoas;


import com.unidev.polyembeddedcms.PolyRecord;
import org.springframework.hateoas.ResourceSupport;

public class HateoasPoly extends ResourceSupport {

    private PolyRecord poly;

    public static HateoasPoly hateoasPoly(PolyRecord poly) {
        return new HateoasPoly().poly(poly);
    }

    public HateoasPoly poly(PolyRecord poly) {
        this.poly = poly;
        return this;
    }

    public PolyRecord poly() {
        return poly;
    }

    public PolyRecord getPoly() {
        return poly;
    }

    public void setPoly(PolyRecord poly) {
        this.poly = poly;
    }
}
