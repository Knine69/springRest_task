package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.BaseException;
import com.jhuguet.sb_taskv1.app.exceptions.UsernameNotFound;
import com.jhuguet.sb_taskv1.app.services.UserService;
import com.jhuguet.sb_taskv1.app.services.utils.SetUpUtils;
import com.jhuguet.sb_taskv1.app.web.utils.JwtUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GuestControllerTest {

    SetUpUtils utils = new SetUpUtils();
    private AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
    private UserService userService = Mockito.mock(UserService.class);
    private JwtUtils jwtUtils = Mockito.mock(JwtUtils.class);
    private GuestController controller = new GuestController(jwtUtils, authenticationManager, userService);

    @BeforeAll
    public void setUpMocks() throws IOException, UsernameNotFound {
        when(jwtUtils.createJwt(anyString())).thenReturn(utils.sampleAuthAccount());
    }

    @Test
    void loginCorrectlyIfCredentialsMatch() throws IOException, UsernameNotFound {
        assertEquals(utils.sampleAuthAccount().getTimestamp().toString(), controller.login(utils.sampleAuthRequest())
                                                                                    .getTimestamp().toString());
    }

    @Test
    void signInCorrectlyIfUserWasCorrectlyPassed() throws BaseException, IOException {
        assertEquals(utils.sampleAuthAccount().getTimestamp().toString(), controller.signIn(utils.sampleUser())
                                                                                    .getTimestamp().toString());
    }
}