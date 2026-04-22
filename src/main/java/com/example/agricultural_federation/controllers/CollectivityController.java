package com.example.agricultural_federation.controllers;

import com.example.agricultural_federation.dto.CollectivityDto;
import com.example.agricultural_federation.dto.CreateCollectivityDto;
import com.example.agricultural_federation.services.CollectivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {

    private final CollectivityService collectivityService;

    public CollectivityController(CollectivityService collectivityService) {
        this.collectivityService = collectivityService;
    }

    @PostMapping
    public ResponseEntity<List<CollectivityDto>> createCollectivities(@RequestBody List<CreateCollectivityDto> createCollectivityDtos) {
        List<CollectivityDto> createdCollectivities = collectivityService.createCollectivities(createCollectivityDtos);
        return new ResponseEntity<>(createdCollectivities, HttpStatus.CREATED);
    }
}
