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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Sort option for queries. You have to provide at least a list of properties to
 * sort for that must not include {@code null} or empty strings. The order
 * defaults to {@value Order#ASCENDING}.
 * 
 * @author Oliver Gierke - gierke@synyx.de
 */
public class Sort implements Iterable<org.resthub.core.domain.Sort.Property> {

    public static final Order DEFAULT_ORDER = Order.ASCENDING;

    private List<Property> properties;
    private Order order;


    /**
     * Creates a new {@link Sort} instance. Takes a List of {@link Property}
     * 
     * @param properties must not be {@literal null} or contain {@literal null}
     *            or empty strings
     */
    public Sort(List<Property> properties) {

        if (null == properties || properties.isEmpty()) {
            throw new IllegalArgumentException(
                    "You have to provide at least one sort property to sort by!");
        }

        this.properties = properties;
    }


    /**
     * Creates a new {@link Sort} instance. Order defaults to
     * {@value Order#ASCENDING}.
     * 
     * @param properties must not be {@literal null} or contain {@literal null}
     *            or empty strings
     */
    public Sort(String... properties) {

        this(DEFAULT_ORDER, properties);
    }


    /**
     * Creates a new {@link Sort} instance.
     * 
     * @param order defaults to {@value Order#ASCENDING} (for {@literal null}
     *            cases, too)
     * @param properties must not be {@literal null} or contain {@literal null}
     *            or empty strings
     */
    public Sort(Order order, String... properties) {

        if (null == properties || 0 == properties.length) {
            throw new IllegalArgumentException(
                    "You have to provide at least one property to sort by!");
        }

        this.properties = new ArrayList<Property>(properties.length);
        this.order = null == order ? DEFAULT_ORDER : order;

        for (String propertyName : properties) {

            this.properties.add(new Property(order, propertyName));
        }
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Property> iterator() {

        return this.properties.iterator();
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Sort)) {
            return false;
        }

        Sort that = (Sort) obj;

        boolean orderEqual = this.order.equals(that.order);
        boolean propertiesEqual = this.properties.equals(that.properties);

        return orderEqual && propertiesEqual;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;

        result = 31 * result + order.hashCode();
        result = 31 * result + properties.hashCode();

        return result;
    }

    /**
     * Property implements the pairing of an {@code Order} and a property. It is
     * used to provide input for {@link Sort}
     * 
     * @author Joachim Uhrlaß - ecapot@gmail.com
     * @author Oliver Gierke - gierke@synyx.de
     */
    public static class Property {

        private Order order;
        private String property;


        /**
         * Creates a new {@link Property} instance. if order is {@code null}
         * then order defaults to {@value Sort.DEFAULT_ORDER}
         * 
         * @param order can be {@code null}
         * @param property must not be {@code null} or empty
         */
        public Property(Order order, String property) {

            if (property == null || "".equals(property.trim())) {
                throw new IllegalArgumentException(
                        "Property must not null or empty!");
            }

            this.order = order == null ? DEFAULT_ORDER : order;
            this.property = property;
        }


        /**
         * Creates a new {@link Property} instance. Takes a single property.
         * Order defaults to {@value Sort.DEFAULT_ORDER}
         * 
         * @param property - must not be {@code null} or empty
         */
        public Property(String property) {

            this(DEFAULT_ORDER, property);
        }


        /**
         * Returns the order the property shall be sorted for.
         * 
         * @return
         */
        public Order getOrder() {

            return order;
        }


        public String getName() {

            return property;
        }


        /**
         * Returns whether sorting for this property shall be ascending.
         * 
         * @return
         */
        public boolean isAscending() {

            return this.order.equals(Order.ASCENDING);
        }


        /**
         * Returns a new {@link Property} with the given {@link Order}.
         * 
         * @param order
         * @return
         */
        public Property with(Order order) {

            return new Property(order, this.property);
        }


        /**
         * Returns a new {@link Sort} instance for the given properties.
         * 
         * @param properties
         * @return
         */
        public Sort withProperties(String... properties) {

            return new Sort(this.order, properties);
        }


        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {

            int result = 17;

            result = 31 * result + order.hashCode();
            result = 31 * result + property.hashCode();

            return result;
        }


        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Property)) {
                return false;
            }

            Property that = (Property) obj;

            return this.order.equals(that.order)
                    && this.property.equals(that.property);
        }
    }
}
