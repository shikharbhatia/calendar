package com.postman.calendar.services;

import com.postman.calendar.models.Slot;
import com.postman.calendar.models.User;
import com.postman.calendar.repositories.SlotRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CalendarServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    CalendarService calendarService;

    User user;
    @Before
    public void setup(){
        user = new User("test@test.com", "test", "user", "xyz");
    }
    @Test
    public void itShouldFindAvailableSlots(){
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

        List<Slot> actual = calendarService.findAvailableSlots(user);
        Assert.assertEquals(slotList, actual);
    }

    @Test
    public void itShouldDisplayAllBookedSlots(){
        List<Slot> slotList = new ArrayList<>();
        Slot slot1 = new Slot();
        slot1.setStartTime("0200 hrs");
        slot1.setEndTime("0300 hrs");
        slotList.add(slot1);
        slot1.setBookedByUser("John Doe");

        Slot slot2 = new Slot();
        slot2.setStartTime("0200 hrs");
        slot2.setEndTime("0300 hrs");
        slotList.add(slot2);

        user.setSlotList(slotList);

        List<Slot> actual = calendarService.displayAllBookedSlots(user);
        List<Slot> expected = new ArrayList<>();
        expected.add(slot1);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void itShouldBookAParticularSlot(){
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

        User requestedBy = new User("xyz@test.com", "John", "Doe", "xyz");

        when(userService.findByEmail(anyString())).thenReturn(requestedBy);
        when(slotRepository.save(any())).thenReturn(slot1);

        Boolean actual = calendarService.bookASlot(user, slot1.getSlotId(), "xyz@gmail.com");

        Assert.assertEquals(true, actual);
    }

    @Test
    public void itShouldNotBookOwnSlot(){
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

        User requestedBy = new User("test@test.com", "John", "Doe", "xyz");

        when(userService.findByEmail(anyString())).thenReturn(requestedBy);

        Boolean actual = calendarService.bookASlot(user, slot1.getSlotId(), "xyz@gmail.com");

        Assert.assertEquals(false, actual);
    }
}
