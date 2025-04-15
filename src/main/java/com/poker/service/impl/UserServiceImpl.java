package com.poker.service.impl;

import com.poker.model.User;
import com.poker.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {
    private final Map<String, User> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        // 添加默认用户
        createUser("admin", "admin123");
        createUser("player1", "player123");
        createUser("player2", "player123");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        
        String role = username.equals("admin") ? "ADMIN" : "PLAYER";
        
        return org.springframework.security.core.userdetails.User
            .builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)))
            .build();
    }

    @Override
    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setChips(1000); // 默认筹码
        users.put(username, user);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return users.get(username);
    }
} 