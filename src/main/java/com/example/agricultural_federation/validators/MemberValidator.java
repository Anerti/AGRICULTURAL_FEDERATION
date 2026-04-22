package com.example.agricultural_federation.validators;

import com.example.agricultural_federation.dto.CreateMemberStructureDto;
import com.example.agricultural_federation.exceptions.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberValidator {

    private static final int MIN_REFEREES = 2;

    public void validate(CreateMemberStructureDto request) {
        if (!request.isRegistrationFeePaid()) {
            throw new BadRequestException("Registration fee has not been paid.");
        }
        if (!request.isMembershipDuesPaid()) {
            throw new BadRequestException("Membership dues have not been paid.");
        }

        List<String> referees = request.getReferees();
        if (referees == null || referees.size() < MIN_REFEREES) {
            throw new BadRequestException(
                    "At least " + MIN_REFEREES + " referees are required to register a member.");
        }
    }
}
