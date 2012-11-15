package org.resthub.validation.model;

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

    @NotNull
    public String getNotNull() {
        return notNull;
    }

    public void setNotNull(String notNull) {
        this.notNull = notNull;
    }

    @Null
    public Integer getaNull() {
        return aNull;
    }

    public void setaNull(Integer aNull) {
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
}
