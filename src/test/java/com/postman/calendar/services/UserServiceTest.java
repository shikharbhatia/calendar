package com.postman.calendar.services;

import com.postman.calendar.models.Slot;
import com.postman.calendar.models.User;
import com.postman.calendar.repositories.SlotRepository;
import com.postman.calendar.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private SlotRepository slotRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void itShouldTestFindByEmail() {
        User actualUser = new User("test@test.com", "test", "user", "xyz");
        when(userRepository.findByEmailId(anyString())).thenReturn(actualUser);
        User expected = userService.findByEmail("test@test.com");
        Assert.assertEquals(expected, actualUser);
    }

    @Test
    public void itShouldTestProcessUserRegistration(){
        User actualUser = new User("test@test.com", "test", "user", "xyz");
        when(userRepository.findByEmailId(anyString())).thenReturn(null);
        when(userRepository.save(any())).thenReturn(actualUser);
        when(passwordEncoder.encode(anyString())).thenReturn("randomString");
        Boolean actual = userService.processUserRegistration(actualUser);

        Assert.assertEquals(true, actual);
    }

    @Test
    public void itShouldTestProcessUserRegistrationForAlreadyExistingUser(){
        User actualUser = new User("test@test.com", "test", "user", "xyz");
        when(userRepository.findByEmailId(anyString())).thenReturn(actualUser);
        Boolean actual = userService.processUserRegistration(actualUser);
        Assert.assertEquals(false, actual);
    }

    @Test
    public void itShouldTestAuthenticateUser() {
        User actualUser = new User("test@test.com", "test", "user", "xyz");
        when(userRepository.findByEmailId(anyString())).thenReturn(actualUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        Boolean actual = userService.authenticateUser("test@test.com", "xyz");
        Assert.assertEquals(true, actual);
    }

    @Test
    public void itShouldTestAuthenticateUserWhenUserIsNotCreated() {
        when(userRepository.findByEmailId(anyString())).thenReturn(null);
        Boolean actual = userService.authenticateUser("test@test.com", "xyz");
        Assert.assertEquals(false, actual);
    }

    @Test
    public void itShouldTestDefineAvailableSlots(){
        List<Slot> slotList = new ArrayList<>();
        Slot slot1 = new Slot();
        slot1.setStartTime("0200 hrs");
        slot1.setEndTime("0300 hrs");
        slotList.add(slot1);

        Slot slot2 = new Slot();
        slot2.setStartTime("0200 hrs");
        slot2.setEndTime("0300 hrs");
        slotList.add(slot2);

        User user = new User("test@test.com", "test", "user", "xyz");

        when(userRepository.findByEmailId(anyString())).thenReturn(user);
        when(slotRepository.saveAll(any())).thenReturn(slotList);
        when(userRepository.save(any())).thenReturn(user);

        Boolean actual = userService.defineAvailableSlots("test@test.com", slotList);

        Assert.assertEquals(true, actual);
    }
    @Test
    public void itShouldTestDefineAvailableSlotsWhenSlotsAlreadyAdded(){
        User user = new User("test@test.com", "test", "user", "xyz");
        List<Slot> slotList = new ArrayList<>();
        Slot slot1 = new Slot();
        slot1.setStartTime("0200 hrs");
        slot1.setEndTime("0300 hrs");
        slotList.add(slot1);

        Slot slot2 = new Slot();
        slot2.setStartTime("0200 hrs");
        slot2.setEndTime("0300 hrs");
        slotList.add(slot2);

        user.setSlotList(slotList);

        when(userRepository.findByEmailId(anyString())).thenReturn(user);

        Boolean actual = userService.defineAvailableSlots("test@test.com", null);
        Assert.assertEquals(false, actual);
    }
}
