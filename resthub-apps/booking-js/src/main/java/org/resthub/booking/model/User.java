package org.resthub.booking.model;

import javax.persistence.*;
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
@Table(name="Customer")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User extends Resource {
    
    private String username;
    private String password;
    private String name;

    public User() {
    }
   
    public User(String name, String password, String username) {
        this.name = name;
        this.password = password;
        this.username = username;
    }

    @Override
    public String toString()  {
        return "User(" + getUsername() + ")";
    }

    @NotNull
    @Size(min=4,max=15)
    @Pattern(regexp="^\\w*$", message="Not a valid username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull
    @Size(min=4,max=15)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotNull
    @Size(max=100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
