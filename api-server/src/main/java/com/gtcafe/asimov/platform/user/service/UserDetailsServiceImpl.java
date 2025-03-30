package com.gtcafe.asimov.platform.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.platform.user.model.UserEntity;
import com.gtcafe.asimov.platform.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity member = memberRepository.findByUsername(username);
        if (member == null) {
            log.error("Can't find member: [{}]", username);
            throw new UsernameNotFoundException("Can't find member: " + username);
        }

        List<SimpleGrantedAuthority> authorities = member.getAuthorities()
                .stream()
                .map(auth -> new SimpleGrantedAuthority(auth.name()))
                .toList();

        return User
                .withUsername(username)
                .password(member.getPassword())
                .authorities(authorities)
                .build();
    }
}