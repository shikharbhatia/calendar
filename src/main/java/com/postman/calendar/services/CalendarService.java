package com.postman.calendar.services;

import com.postman.calendar.models.Slot;
import com.postman.calendar.models.User;
import com.postman.calendar.repositories.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    @Autowired
    private UserService userService;

    @Autowired
    private SlotRepository slotRepository;

    public List<Slot> findAvailableSlots(User user) {
        List<Slot>availableSlots = user.getSlotList().stream()
                .filter(slot -> slot.getBookedByUser() == null)
                .collect(Collectors.toList());
        return availableSlots;
    }

    public List<Slot> displayAllBookedSlots(User user) {
        List<Slot>bookedSlots = user.getSlotList().stream()
                .filter(slot -> slot.getBookedByUser() != null)
                .collect(Collectors.toList());
        return bookedSlots;
    }

    public Boolean bookASlot(User user, String slotId, String requestedByUsername) {
        List<Slot>slots = user.getSlotList();

        Slot slot = slots.stream()
                .filter(s->s.getSlotId().equals(slotId))
                .findFirst()
                .orElse(null);

        if(slot == null)
            return false;
        if(slot.getBookedByUser() != null){
            return false;
        }
        User requestedBy = userService.findByEmail(requestedByUsername);
        if(requestedBy.getEmailId().equals(user.getEmailId()))
            return false;
        slot.setBookedByUser(requestedBy.getName());
        slotRepository.save(slot);
        return true;
    }

}
