package com.example.agricultural_federation.services;

import com.example.agricultural_federation.repositories.MemberRepository;
import com.example.agricultural_federation.dto.CreateMemberStructureDto;
import com.example.agricultural_federation.entities.Member;
import com.example.agricultural_federation.exceptions.NotFoundException;
import com.example.agricultural_federation.validators.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    public List<Member> createAll(List<CreateMemberStructureDto> requests) {
        List<Member> created = new ArrayList<>();
        for (CreateMemberStructureDto request : requests) {
            created.add(create(request));
        }
        return created;
    }

    private Member create(CreateMemberStructureDto request) {
        memberValidator.validate(request);

        try {
            List<String> refereeIds = request.getReferees();
            List<Member> refereeMembers = memberRepository.findAllByIds(refereeIds);
            if (refereeMembers.size() != refereeIds.size()) {
                throw new NotFoundException("One or more referee members were not found.");
            }

            Member member = new Member();
            member.setId(UUID.randomUUID().toString());
            member.setFirstName(request.getFirstName());
            member.setLastName(request.getLastName());
            member.setBirthDate(request.getBirthDate());
            member.setGender(request.getGender());
            member.setAddress(request.getAddress());
            member.setProfession(request.getProfession());
            member.setPhoneNumber(request.getPhoneNumber());
            member.setEmail(request.getEmail());
            member.setRole(request.getRole());
            member.setJoinedAt(new Date());
            member.setCollectivityId(request.getCollectivityIdentifier());

            memberRepository.save(member);
            memberRepository.saveReferrers(member.getId(), refereeIds);

            member.setReferees(refereeMembers);
            return member;

        } catch (SQLException e) {
            throw new RuntimeException("Database error while creating member: " + e.getMessage(), e);
        }
    }
}
