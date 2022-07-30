package com.example.service;

import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;





import javax.annotation.PostConstruct;
import java.util.*;

@Transactional
@Service
public class UserServiceImp implements UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }


    @PostConstruct
    public void add() {
        User user = new User("Tony", "Stark", 33, "ironman@mail.ru", "ironman");
        Role role1 = roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
        Role role2 = roleRepository.saveAndFlush(new Role("ROLE_USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleSet(Collections.singleton(role1));
        userRepository.saveAndFlush(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void update(User user) {
        userRepository.saveAndFlush(user);
    }


    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (Role role : user.getRoleSet()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return user;
    }
}
