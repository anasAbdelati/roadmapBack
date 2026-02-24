package org.roadmapBack.service;

import org.roadmapBack.data.User;
import org.roadmapBack.dto.RegisterRequestDto;
import org.roadmapBack.dto.UserDto;
import org.roadmapBack.exceptions.UserAlreadyExistsException;
import org.roadmapBack.exceptions.UserNotFoundException;
import org.roadmapBack.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public UserDto register(RegisterRequestDto request) {
        if(userRepository.findUserByEmail(request.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        final var user=User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        final var savedUser = userRepository.save(user);
        return UserDto.builder().name(savedUser.getName()).email(savedUser.getEmail()).build();
    }

    public User findUserByEmail(String email){
        final var user=userRepository.findUserByEmail(email);
        if(user == null) {
            throw new UserNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findUserByEmail(email);
    }
}
