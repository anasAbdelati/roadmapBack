package org.roadmapBack.service;

import org.roadmapBack.data.User;
import org.roadmapBack.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        if(userRepository.findUserByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User Login(String email, String password){
        final var user=findUserByEmail(email);
        if(user == null) {
            throw new RuntimeException("Email or password are not correct or account doesn't exist");
        }
        final var hashedPassword=passwordEncoder.encode(password);
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Email or password are not correct");
        }
        return user;
    }

    //TODO refactor and change exceptions
    public User findUserByEmail(String email) {
        final var user=userRepository.findUserByEmail(email);
        if(user == null) {
            throw new RuntimeException("Email or password are not correct or account doesn't exist");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email);
    }
}
