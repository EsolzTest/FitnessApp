package com.esolz.fitnessapp.datatype;

/**
 * Created by ltp on 03/07/15.
 */
public class ExerciseSetsDatatype {
    String reps, kg;

    public ExerciseSetsDatatype(String reps, String kg) {
        this.reps = reps;
        this.kg = kg;
    }

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
}
