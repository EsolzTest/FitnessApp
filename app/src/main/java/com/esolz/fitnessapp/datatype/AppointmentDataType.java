package com.esolz.fitnessapp.datatype;

/**
 * Created by ltp on 06/07/15.
 */
public class AppointmentDataType {
    String trainer_name, trainer_image, trainer_about, trainer_address, booked_by, booked_date, booking_time_start, booking_time_end,
            cancel_status, status, booking_date, program_name;

    public AppointmentDataType(String trainer_name, String trainer_image, String trainer_about, String trainer_address, String booked_by, String booked_date, String booking_time_start, String booking_time_end, String cancel_status, String status, String booking_date, String program_name) {
        this.trainer_name = trainer_name;
        this.trainer_image = trainer_image;
        this.trainer_about = trainer_about;
        this.trainer_address = trainer_address;
        this.booked_by = booked_by;
        this.booked_date = booked_date;
        this.booking_time_start = booking_time_start;
        this.booking_time_end = booking_time_end;
        this.cancel_status = cancel_status;
        this.status = status;
        this.booking_date = booking_date;
        this.program_name = program_name;
    }

    public String getTrainer_name() {
        return trainer_name;
    }

    public void setTrainer_name(String trainer_name) {
        this.trainer_name = trainer_name;
    }

    public String getTrainer_image() {
        return trainer_image;
    }

    public void setTrainer_image(String trainer_image) {
        this.trainer_image = trainer_image;
    }

    public String getTrainer_about() {
        return trainer_about;
    }

    public void setTrainer_about(String trainer_about) {
        this.trainer_about = trainer_about;
    }

    public String getTrainer_address() {
        return trainer_address;
    }

    public void setTrainer_address(String trainer_address) {
        this.trainer_address = trainer_address;
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

    public String getCancel_status() {
        return cancel_status;
    }

    public void setCancel_status(String cancel_status) {
        this.cancel_status = cancel_status;
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