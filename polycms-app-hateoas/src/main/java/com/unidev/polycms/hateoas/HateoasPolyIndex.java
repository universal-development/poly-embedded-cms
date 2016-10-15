package com.unidev.polycms.hateoas;


import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

public class HateoasPolyIndex extends ResourceSupport {

    private HateoasPoly metadata;
    private List<HateoasPoly> list;

    public static HateoasPolyIndex hateoasPolyIndex() {
        return new HateoasPolyIndex();
    }

    public HateoasPolyIndex metadata(HateoasPoly metadata) {
        this.metadata = metadata;
        return this;
    }

    public HateoasPolyIndex list(List<HateoasPoly> list) {
        this.list = list;
        return this;
    }

    public HateoasPolyIndex add(HateoasPoly hateoasPoly) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(hateoasPoly);
        return this;
    }

    public HateoasPoly getMetadata() {
        return metadata;
    }

    public void setMetadata(HateoasPoly metadata) {
        this.metadata = metadata;
    }

    public List<HateoasPoly> getList() {
        return list;
    }

    public void setList(List<HateoasPoly> list) {
        this.list = list;
    }
}
