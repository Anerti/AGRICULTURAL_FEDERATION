package com.example.agricultural_federation.services;

import com.example.agricultural_federation.entities.MembershipFee;
import com.example.agricultural_federation.exceptions.NotFoundException;
import com.example.agricultural_federation.repositories.CooperativeRepository;
import com.example.agricultural_federation.repositories.MembershipFeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipFeeService {

    private final MembershipFeeRepository membershipFeeRepository;
    private final CooperativeRepository cooperativeRepository;

    public MembershipFeeService(MembershipFeeRepository membershipFeeRepository, CooperativeRepository cooperativeRepository) {
        this.membershipFeeRepository = membershipFeeRepository;
        this.cooperativeRepository = cooperativeRepository;
    }

    public List<MembershipFee> getFeesByCoopId(String cooperativeId) {
        if (cooperativeRepository.findById(cooperativeId) == null) {
            throw new NotFoundException("Cooperative not found: " + cooperativeId);
        }

        return membershipFeeRepository.findByCooperativeId(cooperativeId).stream().toList();
    }
}
