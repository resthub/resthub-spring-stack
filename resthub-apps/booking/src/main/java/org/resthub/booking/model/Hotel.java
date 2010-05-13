package org.resthub.booking.model;

import javax.persistence.*;
import java.math.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.resthub.core.model.Resource;

@Entity
public class Hotel extends Resource {
    
    @NotNull
    @Size(max=50)
    private String name;
    
    @Size(min=100)
    private String address;
    
    @NotNull
    @Size(max=40)
    private String city;
    
    @NotNull
    @Size(min=2,max=15)
    private String state;
    
    @NotNull
    @Size(min=6,max=6)
    private String zip;
    
    @NotNull
    @Size(min=2,max=40)
    private String country;
    
    @Column(precision=6, scale=2)
    private BigDecimal price;

    @Override
    public String toString() {
        return "Hotel(" + getName() + "," + getAddress() + "," + getCity() + "," + getZip() + ")";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
}
