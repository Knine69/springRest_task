package com.jhuguet.sb_taskv1.app.services.impl;

import com.jhuguet.sb_taskv1.app.exceptions.UsernameNotFound;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomUserDetailsServiceImplTest {

    private static final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private static CustomUserDetailsServiceImpl userService = new CustomUserDetailsServiceImpl(userRepository);
    private SetUpUtils utils = new SetUpUtils();

    @BeforeAll
    public void setUpMocks() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(utils.sampleUser()));
    }

    @Test
    void loadUserByUsernameCorrectlyIfUsernameIsCorrect() {
        assertEquals(utils.sampleUserDetails().getUsername(), userService.loadUserByUsername("username").getUsername());
    }

    @Test
    void getUserByUsernameIfUserNameIsCorrect() throws UsernameNotFound {
        assertEquals(utils.sampleUser().getUsername(), userService.getUserByUsername("username").getUsername());
    }
}