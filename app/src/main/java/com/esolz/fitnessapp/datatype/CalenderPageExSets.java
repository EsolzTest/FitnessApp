package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 19/6/15.
 */
public class CalenderPageExSets {
    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getKg() {
        return kg;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }

    String reps, kg;

    public CalenderPageExSets(String reps, String kg) {

        this.kg = kg;
        this.reps = reps;

    }
}
