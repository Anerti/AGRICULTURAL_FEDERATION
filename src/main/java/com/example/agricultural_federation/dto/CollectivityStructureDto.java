package com.example.agricultural_federation.dto;

import com.example.agricultural_federation.entities.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CollectivityStructureDto {
    private Member president;
    private Member vicePresident;
    private Member treasurer;
    private Member secretary;
}