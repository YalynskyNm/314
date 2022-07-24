package com.example.service;

import com.example.model.Role;
import com.example.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;



import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {

    void add(User user);
    void delete(User user);
    void update(User user, Set<Role> roleSet);
    List<User> listUsers();
    User getUserById(long id);
    User getUserByUsername(String username);
}
