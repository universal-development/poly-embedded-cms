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
package com.unidev.polyembeddedcms;

/**
 * Query object for new new polys
 */
public class PolyQuery {

    private String tag;
    private String category;
    private Long page = 0L;

    private Long itemPerPage = PolyConstants.DEFAULT_ITEM_PER_PAGE;

    public PolyQuery() {}

    public PolyQuery(String tag, String category, Long page) {
        this.tag = tag;
        this.category = category;
        this.page = page;
    }

    public static PolyQuery query() {
        return new PolyQuery();
    }

    public <T extends PolyQuery> T tag(String tag) {
        setTag(tag);
        return (T) this;
    }

    public <T extends PolyQuery> T category(String category) {
        setCategory(category);
        return (T) this;
    }

    public Long page() {
        return page;
    }

    public <T extends PolyQuery> T page(Long page) {
        setPage(page);
        return (T) this;
    }

    public <T extends PolyQuery> T itemPerPage(Long itemPerPage) {
        setItemPerPage(itemPerPage);
        return (T) this;
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

    public Long getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(Long itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PolyQuery{");
        sb.append("tag='").append(tag).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", page=").append(page);
        sb.append(", itemPerPage=").append(itemPerPage);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PolyQuery polyQuery = (PolyQuery) o;

        if (tag != null ? !tag.equals(polyQuery.tag) : polyQuery.tag != null) return false;
        if (category != null ? !category.equals(polyQuery.category) : polyQuery.category != null) return false;
        if (page != null ? !page.equals(polyQuery.page) : polyQuery.page != null) return false;
        return itemPerPage != null ? itemPerPage.equals(polyQuery.itemPerPage) : polyQuery.itemPerPage == null;

    }

    @Override
    public int hashCode() {
        int result = tag != null ? tag.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (page != null ? page.hashCode() : 0);
        result = 31 * result + (itemPerPage != null ? itemPerPage.hashCode() : 0);
        return result;
    }
}
