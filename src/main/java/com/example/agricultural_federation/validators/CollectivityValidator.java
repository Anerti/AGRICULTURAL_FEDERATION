package com.example.agricultural_federation.validators;

import com.example.agricultural_federation.dto.CreateCollectivityDto;
import com.example.agricultural_federation.dto.CreateCollectivityStructureDto;
import com.example.agricultural_federation.entities.Member;
import com.example.agricultural_federation.exceptions.BadRequestException;
import com.example.agricultural_federation.exceptions.NotFoundException;
import com.example.agricultural_federation.repositories.MemberRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CollectivityValidator {

    private static final int MIN_MEMBERS = 10;
    private static final int MIN_SENIOR_MEMBERS = 5;
    private static final int SENIORITY_MONTHS = 6;

    private final MemberRepository memberRepository;

    public CollectivityValidator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void validate(CreateCollectivityDto dto) {
        validateLocationAndSpecialty(dto);
        validateFederationApproval(dto);
        validateStructure(dto);

        List<Member> members = findAndValidateMembers(dto.getMembers());
        validateMemberRequirements(members);
        validateStructureMembers(dto.getStructure(), members);
    }

    private void validateLocationAndSpecialty(CreateCollectivityDto dto) {
        if (dto.getLocation() == null || dto.getLocation().trim().isEmpty()) {
            throw new BadRequestException("Location is required for collectivity");
        }
        if (dto.getSpecialty() == null || dto.getSpecialty().trim().isEmpty()) {
            throw new BadRequestException("Specialty is required for collectivity");
        }
    }

    private void validateFederationApproval(CreateCollectivityDto dto) {
        if (dto.getFederationApproval() == null || !dto.getFederationApproval()) {
            throw new BadRequestException("Collectivity requires federation approval");
        }
    }

    private void validateStructure(CreateCollectivityDto dto) {
        CreateCollectivityStructureDto structure = dto.getStructure();
        if (structure == null) {
            throw new BadRequestException("Collectivity structure is required (president, vice-president, treasurer, secretary)");
        }
        if (structure.getPresident() == null) {
            throw new BadRequestException("President position must be occupied");
        }
        if (structure.getVicePresident() == null) {
            throw new BadRequestException("Vice-President position must be occupied");
        }
        if (structure.getTreasurer() == null) {
            throw new BadRequestException("Treasurer position must be occupied");
        }
        if (structure.getSecretary() == null) {
            throw new BadRequestException("Secretary position must be occupied");
        }
    }

    private List<Member> findAndValidateMembers(List<String> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            throw new BadRequestException("At least 10 members are required");
        }

        List<Member> foundMembers = new ArrayList<>();
        Set<String> seenIds = new HashSet<>();

        for (String memberId : memberIds) {
            if (seenIds.contains(memberId)) {
                throw new BadRequestException("Duplicate member ID: " + memberId);
            }
            try {
                Member member = memberRepository.findById(memberId);
                foundMembers.add(member);
                seenIds.add(memberId);
            } catch (NotFoundException e) {
                throw new NotFoundException("Member not found with id: " + memberId);
            }
        }

        return foundMembers;
    }

    private void validateMemberRequirements(List<Member> members) {
        if (members.size() < MIN_MEMBERS) {
            throw new BadRequestException("Collectivity must have at least " + MIN_MEMBERS + " members, found: " + members.size());
        }

        int seniorCount = countSeniorMembers(members);
        if (seniorCount < MIN_SENIOR_MEMBERS) {
            throw new BadRequestException("At least " + MIN_SENIOR_MEMBERS + " members must have seniority of " + SENIORITY_MONTHS + " months, found: " + seniorCount);
        }
    }

    private int countSeniorMembers(List<Member> members) {
        LocalDate seniorityThreshold = LocalDate.now().minusMonths(SENIORITY_MONTHS);
        int count = 0;

        for (Member member : members) {
            if (member.getJoinedAt() != null) {
                LocalDate joinDate = member.getJoinedAt().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                if (joinDate.isBefore(seniorityThreshold) || joinDate.isEqual(seniorityThreshold)) {
                    count++;
                }
            }
        }

        return count;
    }

    private void validateStructureMembers(CreateCollectivityStructureDto structure, List<Member> members) {
        Set<String> memberIds = new HashSet<>();
        for (Member member : members) {
            memberIds.add(member.getId());
        }

        validateStructureMember(structure.getPresident(), memberIds, "President");
        validateStructureMember(structure.getVicePresident(), memberIds, "Vice-President");
        validateStructureMember(structure.getTreasurer(), memberIds, "Treasurer");
        validateStructureMember(structure.getSecretary(), memberIds, "Secretary");
    }

    private void validateStructureMember(String memberId, Set<String> validMemberIds, String position) {
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new BadRequestException(position + " position must be occupied by a member");
        }
        if (!validMemberIds.contains(memberId)) {
            throw new NotFoundException(position + " member not found in the provided members list");
        }
    }
}
