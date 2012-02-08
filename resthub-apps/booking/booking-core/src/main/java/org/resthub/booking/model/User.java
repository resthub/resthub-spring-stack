package org.resthub.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Email;

/**
 * Hotel Booking User
 * 
 * @author karesti
 */
@Entity
@Table(name = "customer")
public class User {

    public static final String ALL = "User.all";
    public static final String BY_USERNAME_OR_EMAIL = "User.byUserNameOrEmail";
    public static final String BY_CREDENTIALS = "User.byCredentials";

    private Long id;
    private String username;
    private String fullname;
    private String email;
    private String password;

    public User() {
        super();
    }

    public User(final String fullname, final String username, final String email) {
        super();
        this.fullname = fullname;
        this.username = username;
        this.email = email;
    }

    public User(final String fullname, final String username, final String email, final String password) {
        this(fullname, username, email);
        this.password = password;
    }

    public User(Long id, String username, String fullname, String email, String password) {
        super();
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id ");
        builder.append(id);
        builder.append(",");
        builder.append("username ");
        builder.append(username);
        return builder.toString();
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    @NotNull
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NaturalId
    @Column(nullable = false, unique = true)
    @NotNull
    @Size(min = 3, max = 15)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Column(nullable = false)
    @NotNull
    @Size(min = 3, max = 50)
    public String getFullname() {
        return fullname;
    }

    @Column(nullable = false)
    @Size(min = 3, max = 12)
    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}