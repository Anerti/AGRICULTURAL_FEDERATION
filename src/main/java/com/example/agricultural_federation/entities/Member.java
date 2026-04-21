package com.example.agricultural_federation.entities;

import com.example.agricultural_federation.entities.enums.GenderEnum;
import com.example.agricultural_federation.entities.enums.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString

public class Member {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private GenderEnum gender;
    private String address;
    private String profession;
    private String phoneNumber;
    private String email;
    private RoleEnum occupation;
}
