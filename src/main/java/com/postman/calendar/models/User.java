package com.postman.calendar.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;
    @JsonProperty("emailId")
    private String emailId;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("password")
    private String password;

    @OneToMany
    private List<Slot> slotList;

    public User(String emailId, String firstName, String lastName, String password) {
        this.emailId = emailId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getName() {
        return firstName+" "+lastName;
    }

    public String getPassword() {
        return password;
    }

    public List<Slot> getSlotList() {
        return slotList;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSlotList(List<Slot> slotList) {
        this.slotList = slotList;
    }
}