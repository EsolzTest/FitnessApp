package com.esolz.fitnessapp.datatype;

/**
 * Created by ltp on 06/07/15.
 */
public class AppointmentListDataType {
    String id, trainer_name, booked_by, booked_date, booking_time_start, booking_time_end, status, booking_date, program_name;

    public AppointmentListDataType(String id, String trainer_name, String booked_by, String booked_date, String booking_time_start, String booking_time_end, String status, String booking_date, String program_name) {
        this.id = id;
        this.trainer_name = trainer_name;
        this.booked_by = booked_by;
        this.booked_date = booked_date;
        this.booking_time_start = booking_time_start;
        this.booking_time_end = booking_time_end;
        this.status = status;
        this.booking_date = booking_date;
        this.program_name = program_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrainer_name() {
        return trainer_name;
    }

    public void setTrainer_name(String trainer_name) {
        this.trainer_name = trainer_name;
    }

    public String getBooked_by() {
        return booked_by;
    }

    public void setBooked_by(String booked_by) {
        this.booked_by = booked_by;
    }

    public String getBooked_date() {
        return booked_date;
    }

    public void setBooked_date(String booked_date) {
        this.booked_date = booked_date;
    }

    public String getBooking_time_start() {
        return booking_time_start;
    }

    public void setBooking_time_start(String booking_time_start) {
        this.booking_time_start = booking_time_start;
    }

    public String getBooking_time_end() {
        return booking_time_end;
    }

    public void setBooking_time_end(String booking_time_end) {
        this.booking_time_end = booking_time_end;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }
}