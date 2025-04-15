package com.poker.service;

import com.poker.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    User authenticate(String username, String password);
    User createUser(String username, String password);
    User findByUsername(String username);
}