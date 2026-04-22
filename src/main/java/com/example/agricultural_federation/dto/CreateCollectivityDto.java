package com.example.agricultural_federation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateCollectivityDto {
    private String location;
    private String specialty;
    private List<String> members;
    private Boolean federationApproval;
    private CreateCollectivityStructureDto structure;
}