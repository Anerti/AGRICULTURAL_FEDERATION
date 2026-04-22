package com.example.agricultural_federation.config;

import com.example.agricultural_federation.repository.CooperativeRepository;
import com.example.agricultural_federation.repository.MemberRepository;
import com.example.agricultural_federation.services.CooperativeService;
import com.example.agricultural_federation.services.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    public MemberRepository memberRepository(DataSource dataSource) {
        return new MemberRepository(dataSource);
    }

    @Bean
    public CooperativeRepository cooperativeRepository(DataSource dataSource) {
        return new CooperativeRepository(dataSource);
    }

    @Bean
    public MemberService memberService(MemberRepository memberRepository) {
        return new MemberService(memberRepository);
    }

    @Bean
    public CooperativeService cooperativeService(CooperativeRepository cooperativeRepository, MemberRepository memberRepository) {
        return new CooperativeService(cooperativeRepository, memberRepository);
    }
}
