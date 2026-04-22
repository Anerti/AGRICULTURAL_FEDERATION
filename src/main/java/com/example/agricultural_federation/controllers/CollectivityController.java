package com.example.agricultural_federation.controllers;

import com.example.agricultural_federation.dto.CollectivityDto;
import com.example.agricultural_federation.dto.CollectivityIdentifierRequest;
import com.example.agricultural_federation.dto.CreateCollectivityDto;
import com.example.agricultural_federation.entities.MembershipFee;
import com.example.agricultural_federation.services.CollectivityService;
import com.example.agricultural_federation.services.MembershipFeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {

    private final MembershipFeeService membershipFeeService;
    private final CollectivityService collectivityService;


    public CollectivityController(CollectivityService collectivityService, MembershipFeeService membershipFeeService) {
        this.collectivityService = collectivityService;
        this.membershipFeeService = membershipFeeService;
    }

    @PostMapping
    public ResponseEntity<List<CollectivityDto>> createCollectivities(@RequestBody List<CreateCollectivityDto> createCollectivityDtos) throws SQLException {
        List<CollectivityDto> createdCollectivities = collectivityService.createCollectivities(createCollectivityDtos);
        return new ResponseEntity<>(createdCollectivities, HttpStatus.CREATED);
    }

    @PostMapping("/{collectivityId}/identifiers")
    public ResponseEntity<CollectivityDto> assignIdentifiers(
            @PathVariable String collectivityId,
            @RequestHeader("X-Federation-Id") String federationId,
            @RequestBody CollectivityIdentifierRequest request) {
        CollectivityDto collectivity = collectivityService.assignIdentifiers(collectivityId, federationId, request.getName());
        return new ResponseEntity<>(collectivity, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/membershipFees")
    public ResponseEntity<List<MembershipFee>> getMembershipFees(@PathVariable String id) {
        List<MembershipFee> fees = membershipFeeService.getFeesByCoopId(id);
        return ResponseEntity.ok(fees);
    }
}
