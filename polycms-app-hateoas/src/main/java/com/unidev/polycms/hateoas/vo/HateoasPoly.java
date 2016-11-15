package com.unidev.polycms.hateoas.vo;


import com.unidev.polydata.domain.Poly;
import com.unidev.polyembeddedcms.PolyRecord;
import org.springframework.hateoas.ResourceSupport;

public class HateoasPoly extends ResourceSupport {

    private Poly poly;

    public static HateoasPoly hateoasPoly(Poly poly) {
        return new HateoasPoly().poly(poly);
    }

    public HateoasPoly poly(Poly poly) {
        this.poly = poly;
        return this;
    }

    public Poly poly() {
        return poly;
    }

    public Poly getPoly() {
        return poly;
    }

    public void setPoly(PolyRecord poly) {
        this.poly = poly;
    }
}
