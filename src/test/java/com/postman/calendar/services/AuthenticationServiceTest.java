package com.postman.calendar.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    @Mock
    UserService userService;

    @InjectMocks
    AuthenticationService authenticationService;

    @Test
    public void itShouldTestAuthenticateFunction(){
        when(userService.authenticateUser(anyString(), anyString())).thenReturn(true);
        Boolean actual = authenticationService.authenticate("Basic c2hpa2hhcmJoYXRpYTI5QGdtYWlsLmNvbTphZG1pbg==",
                                            userService);
        Assert.assertEquals(true, actual);
    }

    @Test
    public void itShouldTestAuthenticateFunctionWhenNotAuthentic(){
        when(userService.authenticateUser(anyString(), anyString())).thenReturn(false);
        Boolean actual = authenticationService.authenticate("Basic c2hpa2hhcmJoYXRpYTI5QGdtYWlsLmNvbTphZG1pbg==",
                userService);
        Assert.assertEquals(false, actual);
    }


}
