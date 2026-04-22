package com.example.agricultural_federation.controllers;

import com.example.agricultural_federation.dto.CreateMemberStructureDto;
import com.example.agricultural_federation.dto.PaymentRequestDto;
import com.example.agricultural_federation.dto.PaymentResponseDto;
import com.example.agricultural_federation.entities.Member;
import com.example.agricultural_federation.services.MemberService;
import com.example.agricultural_federation.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Member> createMembers(@RequestBody List<CreateMemberStructureDto> requests) {
        return memberService.createAll(requests);
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<PaymentResponseDto> recordPayment(@PathVariable String id, @RequestBody PaymentRequestDto dto) throws SQLException {
        PaymentResponseDto response = paymentService.recordPayment(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}