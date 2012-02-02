package org.resthub.booking.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.NaturalId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 * <p>
 * <strong>Hotel</strong> is the model/entity class that represents a hotel.
 * </p>
 * 
 * @author Gavin King
 * @author Dan Allen
 */
@Indexed
@Entity
@Table(name = "hotel")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hotel {

    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private Integer stars;
    private BigDecimal price;

    public Hotel() {
        super();
    }

    public Hotel(final String name, final String address, final String city, final String state, final String zip,
            final String country) {
        super();
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }

    public Hotel(final int price, final int stars, final String name, final String address, final String city,
            final String state, final String zip, final String country) {
        super();
        this.price = new BigDecimal(price);
        this.stars = stars;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Size(max = 50)
    @NotNull
    @Field(index = Index.TOKENIZED, store = Store.NO)
    @NaturalId
    @Column(unique = true)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Size(max = 100)
    @NotNull
    @Field(index = Index.TOKENIZED, store = Store.NO)
    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    @Size(max = 40)
    @NotNull
    @Field(index = Index.TOKENIZED, store = Store.NO)
    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    @Size(min = 3, max = 6)
    @NotNull
    public String getZip() {
        return zip;
    }

    public void setZip(final String zip) {
        this.zip = zip;
    }

    @Size(min = 2, max = 10)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    @Size(min = 2, max = 40)
    @NotNull
    @Field(index = Index.TOKENIZED, store = Store.NO)
    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    @Min(1)
    @Max(5)
    public Integer getStars() {
        return stars;
    }

    public void setStars(final Integer stars) {
        this.stars = stars;
    }

    @Column(precision = 6, scale = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    @Transient
    public String getLocation() {
        return city + ", " + state + ", " + country;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Hotel other = (Hotel) obj;

        if ((this.id == null) ? (other.getId() != null) : !this.id.equals(other.getId())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.id == null ? 0 : this.id.hashCode());
        return hash;
    }

    @Override
    public String toString() {
        return "Hotel(" + name + "," + address + "," + city + "," + zip + ")";
    }
}
