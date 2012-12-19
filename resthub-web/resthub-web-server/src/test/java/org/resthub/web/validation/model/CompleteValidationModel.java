package org.resthub.web.validation.model;

import org.hibernate.validator.constraints.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class CompleteValidationModel {

    @AssertTrue
    public Boolean assertTrue;

    @AssertFalse
    public boolean assertFalse;

    @DecimalMax("10.5")
    public BigDecimal decimalMax;

    @DecimalMin("0.5")
    public short decimalMin;

    @Digits(integer = 2, fraction = 3)
    public byte digits;

    @Future
    public Calendar future;

    @Max(1000)
    public BigInteger max;

    @Min(1)
    public int min;

    private String notNull;
    private Integer aNull;
    private Collection<Object> size;
    private Date past;
    private String pattern;
    private String creditCardNumber;
    private String email;
    private String length;
    private String notBlank;
    private String notEmpty;
    private String range;
    private String url;

    @NotNull
    public String getNotNull() {
        return notNull;
    }

    public void setNotNull(String notNull) {
        this.notNull = notNull;
    }

    @Null
    public Integer getANull() {
        return aNull;
    }

    public void setANull(Integer aNull) {
        this.aNull = aNull;
    }

    @Size(min = 1, max = 10)
    public Collection<Object> getSize() {
        return size;
    }

    public void setSize(Collection<Object> size) {
        this.size = size;
    }

    @Past
    public Date getPast() {
        return past;
    }

    public void setPast(Date past) {
        this.past = past;
    }

    @Pattern(regexp = ".*")
    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @URL(protocol = "http", host = "localhost", port = 8080)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Range(min = 100, max = 200)
    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @NotEmpty
    public String getNotEmpty() {
        return notEmpty;
    }

    public void setNotEmpty(String notEmpty) {
        this.notEmpty = notEmpty;
    }

    @NotBlank
    public String getNotBlank() {
        return notBlank;
    }

    public void setNotBlank(String notBlank) {
        this.notBlank = notBlank;
    }

    @Length
    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @CreditCardNumber
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
}
