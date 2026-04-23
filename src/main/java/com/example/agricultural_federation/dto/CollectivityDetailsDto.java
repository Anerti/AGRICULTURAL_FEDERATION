package com.example.agricultural_federation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CollectivityDetailsDto {
    private String id;
    private String name;
    private String number;
    private String specialty;
    private LocalDate creationDate;
    private String location;
    private String status;
}