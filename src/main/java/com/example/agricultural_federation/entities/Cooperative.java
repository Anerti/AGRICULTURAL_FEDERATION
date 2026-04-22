package com.example.agricultural_federation.entities;

import com.example.agricultural_federation.dto.CollectivityStructureDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Cooperative {
    private String id;
    private String name;
    private String location;
    private String specialty;
    private Boolean federationApproval;
    private CollectivityStructureDto structure;
    private List<Member> members;
}