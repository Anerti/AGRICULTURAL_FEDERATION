package com.example.agricultural_federation.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString

public class CooperativeFederation {
    private String id;
    private String name;
    private List<Cooperative> cooperatives;
}

