package com.postman.calendar.services;

import com.postman.calendar.models.Slot;
import com.postman.calendar.models.User;
import com.postman.calendar.repositories.SlotRepository;
import com.postman.calendar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmailId(email);
    }

    public Boolean processUserRegistration(User user){
        User existingUser = findByEmail(user.getEmailId());
        if (existingUser !=  null){
            return false;
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return true;
    }
    public Boolean authenticateUser(String userEmail, String password){
        User existingUser = findByEmail(userEmail);
        if(existingUser != null){
            return passwordEncoder.matches(password, existingUser.getPassword());
        }
        return false;
    }

    public Boolean defineAvailableSlots(String emailId, List<Slot> slotList) {
        User user = findByEmail(emailId);
        if(user.getSlotList() != null && user.getSlotList().size() > 0)
            return false;
        List<Slot> slots = (List<Slot>) slotRepository.saveAll(slotList);
        user.setSlotList(slots);
        userRepository.save(user);
        return true;
    }
}
