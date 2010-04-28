/*
 * Copyright 2008-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.resthub.core.domain;



/**
 * Basic Java Bean implementation of {@code Pageable}.
 * 
 * @author Oliver Gierke - gierke@synyx.de
 */
public class PageRequest implements Pageable {

    private int page;
    private int size;
    private Sort sort;


    /**
     * Creates a new {@link PageRequest}. Pages are zero indexed, thus providing
     * 0 for {@code page} will return the first page.
     * 
     * @param size
     * @param page
     */
    public PageRequest(final int page, final int size) {

        if (0 > page) {
            throw new IllegalArgumentException(
                    "Page index must not be less than zero!");
        }

        if (0 > size) {
            throw new IllegalArgumentException(
                    "Page size must not be less than or equal to zero!");
        }

        this.page = page;
        this.size = size;
    }


    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     * 
     * @param page
     * @param size
     * @param order
     * @param properties
     */
    public PageRequest(final int page, final int size, Order order,
            String... properties) {

        this(page, size, new Sort(order, properties));
    }


    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     * 
     * @param page
     * @param size
     * @param sort
     */
    public PageRequest(final int page, final int size, final Sort sort) {

        this(page, size);
        this.sort = sort;
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.synyx.hades.domain.page.Pageable#getNumberOfItems()
     */
    public int getPageSize() {

        return size;
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.synyx.hades.domain.page.Pageable#getPage()
     */
    public int getPageNumber() {

        return page;
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.synyx.hades.domain.page.Pageable#getFirstItem()
     */
    public int getFirstItem() {

        return page * size;
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.synyx.hades.domain.Pageable#getSort()
     */
    public Sort getSort() {

        return sort;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PageRequest)) {
            return false;
        }

        PageRequest that = (PageRequest) obj;

        boolean pageEqual = this.page == that.page;
        boolean sizeEqual = this.size == that.size;

        boolean sortEqual =
                this.sort == null ? that.sort == null : this.sort
                        .equals(that.sort);

        return pageEqual && sizeEqual && sortEqual;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;

        result = 31 * result + page;
        result = 31 * result + size;
        result = 31 * result + (null == sort ? 0 : sort.hashCode());

        return result;
    }
}
