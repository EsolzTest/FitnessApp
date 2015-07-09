package com.esolz.fitnessapp.datatype;

/**
 * Created by ltp on 07/07/15.
 */
public class TimeSlotsDataType {
    String slot_start, slot_end, counter, booking_id, status, statusDependent , trainer_id, booking_date;

    public TimeSlotsDataType() {
        // Default constructor
    }

    public TimeSlotsDataType(String slot_start, String slot_end, String counter,
                             String booking_id, String status, String statusDependent,
                             String trainer_id, String booking_date) {
        this.slot_start = slot_start;
        this.slot_end = slot_end;
        this.counter = counter;
        this.booking_id = booking_id;
        this.status = status;
        this.statusDependent = statusDependent;
        this.trainer_id = trainer_id;
        this.booking_date = booking_date;
    }

    public String getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(String trainer_id) {
        this.trainer_id = trainer_id;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getSlot_start() {
        return slot_start;
    }

    public void setSlot_start(String slot_start) {
        this.slot_start = slot_start;
    }

    public String getSlot_end() {
        return slot_end;
    }

    public void setSlot_end(String slot_end) {
        this.slot_end = slot_end;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDependent() {
        return statusDependent;
    }

    public void setStatusDependent(String statusDependent) {
        this.statusDependent = statusDependent;
    }
}