package com.example.agricultural_federation.services;

import com.example.agricultural_federation.repositories.MemberRepository;
import com.example.agricultural_federation.dto.CollectivityDto;
import com.example.agricultural_federation.dto.CollectivityInformationDto;
import com.example.agricultural_federation.dto.CollectivityStructureDto;
import com.example.agricultural_federation.dto.CreateCollectivityDto;
import com.example.agricultural_federation.entities.Cooperative;
import com.example.agricultural_federation.entities.Member;
import com.example.agricultural_federation.exceptions.BadRequestException;
import com.example.agricultural_federation.exceptions.NotFoundException;
import com.example.agricultural_federation.repositories.CooperativeRepository;
import com.example.agricultural_federation.validators.CollectivityValidator;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CollectivityService {

    private final CooperativeRepository cooperativeRepository;
    private final MemberRepository memberRepository;
    private final CollectivityValidator collectivityValidator;

    public CollectivityService(CooperativeRepository cooperativeRepository, MemberRepository memberRepository, CollectivityValidator collectivityValidator) {
        this.cooperativeRepository = cooperativeRepository;
        this.memberRepository = memberRepository;
        this.collectivityValidator = collectivityValidator;
    }

    public List<CollectivityDto> createCollectivities(List<CreateCollectivityDto> createCollectivityDtos) throws SQLException {
        List<CollectivityDto> createdCollectivities = new ArrayList<>();

        for (CreateCollectivityDto dto : createCollectivityDtos) {
            CollectivityDto created = createCollectivity(dto);
            createdCollectivities.add(created);
        }

        return createdCollectivities;
    }

    private CollectivityDto createCollectivity(CreateCollectivityDto dto) throws SQLException {
        collectivityValidator.validate(dto);

        List<Member> members = findAllMembers(dto);

        Cooperative cooperative = new Cooperative();
        cooperative.setName(generateCollectivityName(dto.getLocation(), dto.getSpecialty()));
        cooperative.setLocation(dto.getLocation());
        cooperative.setSpecialty(dto.getSpecialty());
        cooperative.setFederationApproval(dto.getFederationApproval());
        cooperative.setMembers(members);

        Cooperative savedCooperative = cooperativeRepository.save(cooperative, members);
        savedCooperative.setMembers(members);

        return buildCollectivityDto(savedCooperative, dto.getStructure(), members);
    }

    private List<Member> findAllMembers(CreateCollectivityDto dto) throws SQLException {
        List<Member> members = new ArrayList<>();
        for (String memberId : dto.getMembers()) {
            memberRepository.findById(memberId).ifPresent(members::add);
        }
        return members;
    }

    private String generateCollectivityName(String location, String specialty) {
        return specialty.toUpperCase().replace(" ", "_") + "_" + location.toUpperCase().replace(" ", "_");
    }

    private CollectivityDto buildCollectivityDto(Cooperative cooperative, com.example.agricultural_federation.dto.CreateCollectivityStructureDto structureDto, List<Member> members) {
        CollectivityDto dto = new CollectivityDto();
        dto.setId(cooperative.getId());
        dto.setLocation(cooperative.getLocation());
        dto.setSpecialty(cooperative.getSpecialty());

        CollectivityStructureDto structure = new CollectivityStructureDto();
        structure.setPresident(findMemberById(structureDto.getPresident(), members));
        structure.setVicePresident(findMemberById(structureDto.getVicePresident(), members));
        structure.setTreasurer(findMemberById(structureDto.getTreasurer(), members));
        structure.setSecretary(findMemberById(structureDto.getSecretary(), members));
        dto.setStructure(structure);

        dto.setMembers(members);

        return dto;
    }

    private Member findMemberById(String id, List<Member> members) {
        for (Member member : members) {
            if (member.getId().equals(id)) {
                return member;
            }
        }
        return null;
    }

    public CollectivityDto updateInformations(String collectivityId, CollectivityInformationDto informationDto) {
        collectivityValidator.validate(informationDto);

        Cooperative cooperative = cooperativeRepository.findById(collectivityId);
        if (cooperative == null) {
            throw new NotFoundException("Collectivity not found");
        }

        String number = String.valueOf(informationDto.getNumber());
        if (cooperativeRepository.numberExistsExcludingId(number, collectivityId)) {
            throw new BadRequestException("Number already used by another collectivity");
        }

        if (cooperativeRepository.nameExistsExcludingId(informationDto.getName(), collectivityId)) {
            throw new BadRequestException("Name already used by another collectivity");
        }

        cooperative.setName(informationDto.getName());
        cooperative.setNumber(number);

        Cooperative updated = cooperativeRepository.update(cooperative);

        CollectivityDto dto = new CollectivityDto();
        dto.setId(updated.getId());
        dto.setNumber(updated.getNumber());
        dto.setName(updated.getName());
        dto.setLocation(updated.getLocation());
        dto.setSpecialty(updated.getSpecialty());

        return dto;
    }

    public CollectivityDto assignIdentifiers(String collectivityId, String federationId, String proposedName) {
        if (federationId == null || federationId.isEmpty()) {
            throw new BadRequestException("Missing federation authorization");
        }

        Cooperative cooperative = cooperativeRepository.findById(collectivityId);
        if (cooperative == null) {
            throw new NotFoundException("Collectivity not found");
        }

        if (cooperative.getNumber() != null && !cooperative.getNumber().isEmpty()) {
            throw new BadRequestException("Collectivity already has identifiers assigned");
        }

        if (cooperativeRepository.nameExists(proposedName)) {
            throw new BadRequestException("Name already exists in the system");
        }

        String number = cooperativeRepository.generateNextNumber();

        cooperative.setName(proposedName);
        cooperative.setNumber(number);

        Cooperative updated = cooperativeRepository.update(cooperative);

        CollectivityDto dto = new CollectivityDto();
        dto.setId(updated.getId());
        dto.setNumber(updated.getNumber());
        dto.setName(updated.getName());
        dto.setLocation(updated.getLocation());
        dto.setSpecialty(updated.getSpecialty());

        return dto;
    }
}