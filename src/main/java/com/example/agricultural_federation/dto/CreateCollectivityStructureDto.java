package com.example.agricultural_federation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateCollectivityStructureDto {
    private String president;
    private String vicePresident;
    private String treasurer;
    private String secretary;
}