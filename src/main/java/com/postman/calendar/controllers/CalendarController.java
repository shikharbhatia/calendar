package com.postman.calendar.controllers;

import com.postman.calendar.constants.MessageConstants;
import com.postman.calendar.models.Slot;
import com.postman.calendar.models.User;
import com.postman.calendar.services.CalendarService;
import com.postman.calendar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private UserService userService;

    @Autowired
    private CalendarService calendarService;

    @PostMapping("/registration")
    @ResponseBody
    public ResponseEntity registerUser(@RequestBody User user){
        Boolean result = userService.processUserRegistration(user);
        if(result){
            return new ResponseEntity(MessageConstants.SUCCESSFUL_REGISTRATION, HttpStatus.OK);
        }
        return new ResponseEntity( MessageConstants.USER_EXISTS, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/defineSlots")
    @ResponseBody
    public ResponseEntity defineSlotsForUser(@RequestBody List<Slot> slotList,
                                        @RequestHeader("Authorization") String authorization){
        String username = extractUserName(authorization);
        Boolean result = userService.defineAvailableSlots(username, slotList);
        if(result){
            return new ResponseEntity( MessageConstants.SLOT_DEFINED_SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity( MessageConstants.SLOT_ALREADY_DEFINED, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/availableSlots")
    @ResponseBody
    public ResponseEntity getAllAvailableSlotsForUser(@RequestParam String emailId){

        User user = userService.findByEmail(emailId);
        if(user == null)
            return new ResponseEntity("Incorrect User Requested.", HttpStatus.BAD_REQUEST);
        List<Slot> availableSlots = calendarService.findAvailableSlots(user);
        return new ResponseEntity(availableSlots, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/bookedSlots")
    public ResponseEntity getAllBookedSlotsForUser(@RequestParam String emailId) {

        User user = userService.findByEmail(emailId);
        if(user == null)
            return new ResponseEntity("Incorrect User Requested.", HttpStatus.BAD_REQUEST);
        List<Slot> bookedSlots = calendarService.displayAllBookedSlots(user);
        return new ResponseEntity(bookedSlots, HttpStatus.OK);
    }

    @PostMapping("/bookSlot")
    @ResponseBody
    public ResponseEntity bookSlot(@RequestParam("slotId") String slotId,
                                   @RequestParam("emailId") String emailId,
                                   @RequestHeader("Authorization")String authorisation){

        String username = extractUserName(authorisation);
        User user = userService.findByEmail(emailId);

        if(user == null)
            return new ResponseEntity(MessageConstants.INCORRECT_USER, HttpStatus.BAD_REQUEST);

        Boolean booked = calendarService.bookASlot(user, slotId, username);
        if(!booked){
            return new ResponseEntity(MessageConstants.SLOT_ALREADY_BOOKED, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(MessageConstants.SLOT_BOOK_SUCCESS, HttpStatus.OK);
    }

    private String extractUserName(String authorisationHeader){
        if (null == authorisationHeader)
            return null;

        final String encodedUserPassword = authorisationHeader.replaceFirst("Basic"
                + " ", "");
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(
                    encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final StringTokenizer tokenizer = new StringTokenizer(
                usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        return username;
    }
}
