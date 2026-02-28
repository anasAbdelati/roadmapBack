package org.roadmapBack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.roadmapBack.data.User;
import org.roadmapBack.dto.RegisterRequestDto;
import org.roadmapBack.exceptions.UserAlreadyExistsException;
import org.roadmapBack.exceptions.UserNotFoundException;
import org.roadmapBack.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    public static final String INEXISTANT_MAIL = "inexistant@gmail.com";
    public static final String EXISTANT_MAIL = "existant@gmail.com";

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        when(userRepository.findUserByEmail(INEXISTANT_MAIL)).thenReturn(null);

        final var user1= User.builder().name("ooga").build();
        when(userRepository.findUserByEmail(EXISTANT_MAIL)).thenReturn(user1);
    }

   @Test
    public void registerTest(){
        final var existantUserRequest= RegisterRequestDto.builder().email(EXISTANT_MAIL).build();
        assertThatThrownBy(() -> userService.register(existantUserRequest))
               .isInstanceOf(UserAlreadyExistsException.class)
               .hasMessageContaining("Email already exists");

       when(passwordEncoder.encode("123")).thenReturn("hashedPassword");
       final var user2= User.builder().name("test1").email(INEXISTANT_MAIL).password("123").build();
       when(userRepository.save(any(User.class))).thenReturn(user2);
        final var newUserRequest= RegisterRequestDto.builder().name("test1").email(INEXISTANT_MAIL).password("123").build();
        final var registeredUser=userService.register(newUserRequest);
        assertThat(registeredUser.getName()).isEqualTo("test1");
        assertThat(registeredUser.getEmail()).isEqualTo(INEXISTANT_MAIL);
    }

    @Test
    public void findUserByEmailTest(){

        assertThatThrownBy(() -> userService.findUserByEmail(INEXISTANT_MAIL))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");

        final var existantUser=userService.findUserByEmail(EXISTANT_MAIL);
        assertThat(existantUser).isNotNull();
    }
}