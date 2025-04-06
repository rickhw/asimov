package com.gtcafe.asimov.platform.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.platform.user.model.UserEntity;
import com.gtcafe.asimov.platform.user.repository.UserRepository;


@RestController
@RequestMapping("/api/v1alpha")
public class UserController {

    @Autowired
    private UserRepository repos;

     @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/users")
    public String createMember(@RequestBody UserEntity entity) {
        var encodedPwd = passwordEncoder.encode(entity.getPassword());
        entity.setPassword(encodedPwd);
        entity.setId(null);
        repos.save(entity);
        return entity.getId();
    }

    @GetMapping("/users")
    public List<UserEntity> getMembers() {
        return repos.findAll();
    }

}