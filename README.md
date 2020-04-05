# Calendar - Appointment Booking System
Allows people to define their available slots on a day and other people to book them.

This is Simple Java Application written in Java 8, Springboot, junit testing framework and maven build tool.

# Setup
You can run the application by importing it in any ide like eclipse and intellij or
building it's jar using 
``` mvn clean install ``` 
followed by
``` java -jar jarName.jar ```

# Endpoints and Security

Basic Auth:
All the endpoints except registration have been secured using Basic Auth implemented at the servlet filter. The user needs to enter the credentials to access these endpoints.

The following endpoints are exposed:
1. Register a User : Enables a user to register to the system.
  POST /calendar/registration
2. Define Slots For A User: A user can define free slots for the day using this API.
  POST /calendar/defineSlots
3. Available Slots of a User: This gives all the available slots for the user given his emailId.
  GET /calendar/availableSlots
4. Book A Slot: This enables a user to book another user's available slot. This can be done using available slot Ids that can be found in the Available slots API.
  POST /calendar/bookSlot
5. Booked Slots: This gives the list of all the booked Slots of a user.
  GET /calendar/bookedSlots
  
# Assumptions

1. Communication is not secured. The confidential information passed during signup will be encrypted at client side.
2. Data Validation and consistency, such as, DateTime format, firstName, LastName etc. will be ensured.
3. User can only define his available slots for the day once.
4. The slots defined by user will have consistent data.
5. Any user present in the system can book any other user's slots.
6. Any user can access available and booked slots of another user.
