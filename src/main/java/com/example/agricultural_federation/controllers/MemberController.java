package com.example.agricultural_federation.controllers;

import com.example.agricultural_federation.dto.CreateMemberStructureDto;
import com.example.agricultural_federation.entities.Member;
import com.example.agricultural_federation.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Member> createMembers(@RequestBody List<CreateMemberStructureDto> requests) {
        return memberService.createAll(requests);
    }
}