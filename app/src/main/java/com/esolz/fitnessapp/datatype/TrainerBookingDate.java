package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 25/6/15.
 */
public class TrainerBookingDate  {

    String trainer_id;
    String booking_date;

    public TrainerBookingDate(String trainer_id, String booking_date) {
        this.trainer_id = trainer_id;
        this.booking_date = booking_date;
    }


    public String getTrainee_id()
    {
        return trainer_id;
    }

    public void setTrainee_id(String trainee_id) {
        this.trainer_id = trainer_id;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }
}
