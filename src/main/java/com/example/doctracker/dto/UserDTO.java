package com.example.doctracker.dto;

import com.example.doctracker.entity.Team;

import java.time.LocalDate;
import java.util.Set;

public class UserDTO {

    private String email;
    private LocalDate accountCreationDate;

    public UserDTO(String email, LocalDate accountCreationDate) {
        this.email = email;
        this.accountCreationDate = accountCreationDate;

    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDate getAccountCreationDate() {
        return accountCreationDate;
    }
    public void setAccountCreationDate(LocalDate accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }
}
