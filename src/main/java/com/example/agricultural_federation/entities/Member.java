package com.example.agricultural_federation.entities;

import com.example.agricultural_federation.entities.enums.GenderEnum;
import com.example.agricultural_federation.entities.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Member {
    private String id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private GenderEnum gender;
    private String address;
    private String profession;
    private String phoneNumber;
    private String email;
    private RoleEnum role;
    private Date joinedAt;
}
