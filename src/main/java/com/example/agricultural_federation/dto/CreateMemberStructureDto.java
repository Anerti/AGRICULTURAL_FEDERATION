package com.example.agricultural_federation.dto;

import com.example.agricultural_federation.entities.enums.GenderEnum;
import com.example.agricultural_federation.entities.enums.RoleEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CreateMemberStructureDto {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private GenderEnum gender;
    private String address;
    private String profession;
    private String phoneNumber;
    private String email;
    private RoleEnum role;

    private String collectivityIdentifier;

    private List<String> referees;

    private boolean registrationFeePaid;
    private boolean membershipDuesPaid;
}
