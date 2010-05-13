package org.resthub.booking.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.resthub.core.model.Resource;

@Entity
@Table(name="Customer")
public class User extends Resource {
    
    @NotNull
    @Size(min=4,max=15)
    @Pattern(regexp="^\\w*$", message="Not a valid username")
    private String username;
    
    @NotNull
    @Size(min=5,max=15)
    private String password;
    
    @NotNull
    @Size(max=100)
    private String name;
   
    public User(String name, String password, String username) {
        this.name = name;
        this.password = password;
        this.username = username;
    }

    @Override
    public String toString()  {
        return "User(" + getUsername() + ")";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
