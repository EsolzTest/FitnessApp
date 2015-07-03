package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 18/6/15.
 */
public class SearchResults {


    String slotstart;
    String slotend;
    String counter;
    String status;
    String status2;
    String trainee_id;
    String booking_date;

    boolean checked[];

    public boolean[] getChecked() {
        return checked;
    }

    public void setChecked(boolean[] checked) {
        this.checked = checked;
    }

    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    public String getTrainee_id() {
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

    public SearchResults(String slotstart, String slotend, String counter, String status)
    {
        this.slotstart = slotstart;
        this.slotend = slotend;
        this.counter = counter;
        this.status = status;


    }

    public String getSlotstart() {
        return slotstart;
    }

    public void setSlotstart(String slotstart) {
        this.slotstart = slotstart;
    }

    public String getSlotend() {
        return slotend;
    }

    public void setSlotend(String slotend) {
        this.slotend = slotend;
    }




    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
}
