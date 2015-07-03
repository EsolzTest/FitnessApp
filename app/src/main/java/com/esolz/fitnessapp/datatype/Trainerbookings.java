package com.esolz.fitnessapp.datatype;

import java.io.Serializable;

/**
 * Created by su on 23/6/15.
 */
public class Trainerbookings implements Serializable {

    String trainer_name;

    public String getBookingdate() {
        return bookingdate;
    }

    public void setBookingdate(String bookingdate) {
        this.bookingdate = bookingdate;
    }

    public String getTrainer_name() {
        return trainer_name;
    }

    public void setTrainer_name(String trainer_name) {
        this.trainer_name = trainer_name;
    }

    String bookingdate;

    public Trainerbookings(String trainer_name, String bookingdate) {
        this.trainer_name = trainer_name;
        this.bookingdate = bookingdate;
    }
    public Trainerbookings()
    {

    }
}
