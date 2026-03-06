package com.lav.libtool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @PrePersist
    private void onCreate() {
        if (registrationDate == null) {
            registrationDate = LocalDate.now();
        }
    }

    public Reader(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public void updateDetails(String firstName, String lastName, String email, String phone) {

        if (!firstName.isEmpty()) {
            this.firstName = firstName;
        }

        if (!lastName.isEmpty()) {
            this.lastName = lastName;
        }

        if (!email.isEmpty()) {
            this.email = email;
        }

        if (!phone.isEmpty()) {
            this.phone = phone;
        }
    }

}