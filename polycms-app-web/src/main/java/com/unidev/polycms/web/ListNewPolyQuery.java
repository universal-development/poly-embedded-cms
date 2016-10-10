package com.unidev.polycms.web;

/**
 * Query object for new new polys
 */
public class ListNewPolyQuery {

    private String tag;
    private String category;
    private Long page;

    private Integer itemPerPage = 20;


    public ListNewPolyQuery() {}

    public ListNewPolyQuery(String tag, String category, Long page) {
        this.tag = tag;
        this.category = category;
        this.page = page;
    }

    public static ListNewPolyQuery query() {
        return new ListNewPolyQuery();
    }

    public ListNewPolyQuery withTag(String tag) {
        setTag(tag);
        return this;
    }

    public ListNewPolyQuery withCategory(String category) {
        setCategory(category);
        return this;
    }


    public ListNewPolyQuery withPage(Long page) {
        setPage(page);
        return this;
    }

    public ListNewPolyQuery itemPerPage(Integer itemPerPage) {
        this.itemPerPage = itemPerPage;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Integer getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(Integer itemPerPage) {
        this.itemPerPage = itemPerPage;
    }
}
