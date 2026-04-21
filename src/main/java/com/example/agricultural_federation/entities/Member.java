package com.example.agricultural_federation.entities;

import com.example.agricultural_federation.entities.enums.GenderEnum;
import com.example.agricultural_federation.entities.enums.OccupationEnum;

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
    public String firstName;
    public String lastName;
    public Date birthDate;
    public GenderEnum gender;
    public String address;
    public String profession;
    public String phoneNumber;
    public String email;
    public OccupationEnum occupation;
}
