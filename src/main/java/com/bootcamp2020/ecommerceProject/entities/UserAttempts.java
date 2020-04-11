package com.bootcamp2020.ecommerceProject.entities;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class UserAttempts {
    @Id
    private int id;
    private String username;
    private int attempts;
    private Date lastModified;

    //getter and setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}