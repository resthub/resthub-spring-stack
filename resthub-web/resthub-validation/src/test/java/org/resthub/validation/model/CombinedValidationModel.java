package org.resthub.validation.model;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class CombinedValidationModel {

    private String login;
    private String password;

    @NotNull
    @Pattern(regexp="[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,4}")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @NotNull
    @Size(min=8, max=14)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
