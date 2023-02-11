package kz.test.tz.service.impl;

import kz.test.tz.entity.Role;
import kz.test.tz.entity.User;
import kz.test.tz.pogo.NewUserRequest;
import kz.test.tz.repo.RoleRepository;
import kz.test.tz.repo.UserRepository;
import kz.test.tz.security.jwt.JwtUserFactory;
import kz.test.tz.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with email: " + username + " not found");
        }
        var jwtUser = JwtUserFactory.create(user);
        log.info("IN loadUserByEmail - user with email: {} successfully loaded", username);
        return jwtUser;
    }

    @Transactional
    @Override
    public User registry(NewUserRequest request) {
        var user = new User();
        var role = getRoleUser();
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(User.Status.ACTIVE);
        user.setEmail(request.getEmail());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        var registeredUser = userRepository.save(user);
        log.info("In registry user: {} successfully registered", registeredUser);
        return registeredUser;
    }

    private Role getRoleUser() {
        var role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            Role newRole = new Role();
            newRole.setName("ROLE_USER");
            role = roleRepository.save(newRole);
        }
        return role;
    }

    @Override
    public User findByEmail(String email) {
        var user = userRepository.findByEmail(email);
        log.info("In findByEmail - user: {} found by email: {}", user, email);
        return user;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("In delete - user with id: {} successfully deleted", id);
    }
}
