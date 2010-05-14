package org.resthub.booking.model;

import javax.persistence.*;
import java.math.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.resthub.core.model.Resource;

/**
 * Extending Resource is not mandatory, RESThub GeneriCDao can handle every kind
 * of entities, we use a resource class in order to keep DRY (Don't Repeat Yourself)
 * compliant
 */
@Entity
public class Hotel extends Resource {
    
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private BigDecimal price;

    @Override
    public String toString() {
        return "Hotel(" + getName() + "," + getAddress() + "," + getCity() + "," + getZip() + ")";
    }

    @NotNull
    @Size(max=50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Size(min=100)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotNull
    @Size(max=40)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @NotNull
    @Size(min=2,max=15)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @NotNull
    @Size(min=6,max=6)
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @NotNull
    @Size(min=2,max=40)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(precision=6, scale=2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
}
