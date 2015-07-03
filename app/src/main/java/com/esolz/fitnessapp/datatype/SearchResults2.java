package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 19/6/15.
 */
public class SearchResults2 {

    String trainee_id;
    String booking_date;

    public SearchResults2(String trainee_id, String booking_date) {
        this.trainee_id = trainee_id;
        this.booking_date = booking_date;
    }


    public String getTrainee_id()
    {
        return trainee_id;
    }

    public void setTrainee_id(String trainee_id) {
        this.trainee_id = trainee_id;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }
}
