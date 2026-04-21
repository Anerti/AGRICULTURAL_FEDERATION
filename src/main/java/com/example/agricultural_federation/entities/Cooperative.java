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

public class Cooperative {
    public String id;
    public String location;
    public String structure;
    public List<Member> members;
}
