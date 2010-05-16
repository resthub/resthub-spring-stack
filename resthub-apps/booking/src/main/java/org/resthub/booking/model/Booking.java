package org.resthub.booking.model;


import javax.persistence.*;
import java.util.*;
import java.text.*;
import java.math.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.resthub.core.model.Resource;


/**
 * Extending Resource is not mandatory, RESThub GeneriCDao can handle every kind
 * of entities, we use a resource class in order to keep DRY (Don't Repeat Yourself)
 * compliant
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Booking extends Resource {
    
    private User user;
    private Hotel hotel;
    private Date checkinDate;
    private Date checkoutDate;
    private String creditCard;
    private String creditCardName;
    private int creditCardExpiryMonth;
    private int creditCardExpiryYear;
    private boolean smoking;
    private int beds;

    public Booking() {

    }
    
    public Booking(Hotel hotel, User user) {
        this.hotel = hotel;
        this.user = user;
    }

    @Transient
    public BigDecimal getTotal() {
        return getHotel().getPrice().multiply( new BigDecimal( getNights() ) );
    }

    @Transient
    public int getNights() {
        return (int) ( getCheckoutDate().getTime() - getCheckinDate().getTime() ) / 1000 / 60 / 60 / 24;
    }

    @Transient
    public String getDescription() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return getHotel()==null ? null : getHotel().getName() +
            ", " + df.format( getCheckinDate()) +
            " to " + df.format( getCheckoutDate());
    }

    @Override
    public String toString() {
        return "Booking(" + getUser() + ","+ getHotel() + ")";
    }

    @NotNull
    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NotNull
    @ManyToOne
    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @NotNull
    @Temporal(TemporalType.DATE) 
    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    @NotNull
    @Temporal(TemporalType.DATE)
    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    @NotNull(message="Credit card number is required")
    @Pattern(regexp="^\\d{16}$", message="Credit card number must be numeric and 16 digits long")
    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    @NotNull(message="Credit card name is required")
    @Size(min=3, max=70, message="Credit card name is required")
    public String getCreditCardName() {
        return creditCardName;
    }

    public void setCreditCardName(String creditCardName) {
        this.creditCardName = creditCardName;
    }

    public int getCreditCardExpiryMonth() {
        return creditCardExpiryMonth;
    }

    public void setCreditCardExpiryMonth(int creditCardExpiryMonth) {
        this.creditCardExpiryMonth = creditCardExpiryMonth;
    }

    public int getCreditCardExpiryYear() {
        return creditCardExpiryYear;
    }

    public void setCreditCardExpiryYear(int creditCardExpiryYear) {
        this.creditCardExpiryYear = creditCardExpiryYear;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }
    

}
