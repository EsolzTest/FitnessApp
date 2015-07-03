package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 25/6/15.
 */

    /**
     * Created by su on 18/6/15.
     */
    public class TrainerDetails {


        String slotstart;
        String slotend;
        String counter;
        String status;
        String status2;
        String bookingid;

        public String getBookingid()
        {
            return bookingid;
        }

        public void setBookingid(String bookingid) {
            this.bookingid = bookingid;
        }

        public String getStatus2() {
            return status2;
        }

        public void setStatus2(String status2) {
            this.status2 = status2;
        }



        public TrainerDetails(String slotstart, String slotend, String counter,String bookingid, String status)
        {
            this.slotstart = slotstart;
            this.slotend = slotend;
            this.counter = counter;
            this.bookingid=bookingid;
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


