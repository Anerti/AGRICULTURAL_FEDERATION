package com.example.agricultural_federation.dto;

import com.example.agricultural_federation.entities.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CollectivityDto {
    private String id;
    private String number;
    private String name;
    private String location;
    private String specialty;
    private CollectivityStructureDto structure;
    private List<Member> members;
}