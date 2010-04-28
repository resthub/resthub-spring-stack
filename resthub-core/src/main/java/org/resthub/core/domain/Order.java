package org.resthub.core.domain;

/**
 * Enumeration for sort orders.
 * 
 * @author Oliver Gierke - gierke@synyx.de
 */
public enum Order {

    ASCENDING("asc"), DESCENDING("desc");

    private String jpaValue;


    /**
     * Creates a new instance of {@code Order}.
     * 
     * @param jpaValue
     */
    private Order(String jpaValue) {

        this.jpaValue = jpaValue;
    }


    /**
     * Returns the JPA specific value.
     * 
     * @return the JPA specific value
     */
    public String getJpaValue() {

        return jpaValue;
    }


    /**
     * Returns the {@link Order} enum for the given JPA value.
     * 
     * @param value
     * @return
     */
    public static Order fromJpaValue(String value) {

        for (Order order : Order.values()) {
            if (order.getJpaValue().equals(value)) {
                return order;
            }
        }

        throw new IllegalArgumentException(String.format(
                "Invalid value '%s' given!", value));
    }
}
