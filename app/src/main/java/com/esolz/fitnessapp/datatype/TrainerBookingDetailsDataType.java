package com.esolz.fitnessapp.datatype;

import java.util.ArrayList;

/**
 * Created by ltp on 07/07/15.
 */
public class TrainerBookingDetailsDataType {
    String trainer_id, booking_date;
    ArrayList<TimeSlotsDataType> timeSlotsDataTypeArrayList;

    public TrainerBookingDetailsDataType(String trainer_id, String booking_date, ArrayList<TimeSlotsDataType> timeSlotsDataTypeArrayList) {
        this.trainer_id = trainer_id;
        this.booking_date = booking_date;
        this.timeSlotsDataTypeArrayList = timeSlotsDataTypeArrayList;
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

    public ArrayList<TimeSlotsDataType> getTimeSlotsDataTypeArrayList() {
        return timeSlotsDataTypeArrayList;
    }

    public void setTimeSlotsDataTypeArrayList(ArrayList<TimeSlotsDataType> timeSlotsDataTypeArrayList) {
        this.timeSlotsDataTypeArrayList = timeSlotsDataTypeArrayList;
    }
}