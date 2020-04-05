package com.postman.calendar.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Slot {
    @Id
    private String slotId = UUID.randomUUID().toString();
    private String startTime;
    private String endTime;
    private String bookedByUser = null;

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBookedByUser() {
        return bookedByUser;
    }

    public void setBookedByUser(String bookedByUser) {
        this.bookedByUser = bookedByUser;
    }
}
