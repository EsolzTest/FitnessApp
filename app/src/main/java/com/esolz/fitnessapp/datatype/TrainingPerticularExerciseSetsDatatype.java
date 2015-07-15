package com.esolz.fitnessapp.datatype;

/**
 * Created by ltp on 03/07/15.
 */
public class TrainingPerticularExerciseSetsDatatype {
    String REPS, kg;
    Boolean isEditable;

    public TrainingPerticularExerciseSetsDatatype(String REPS, String kg, Boolean isEditable) {
        this.REPS = REPS;
        this.kg = kg;
        this.isEditable = isEditable;
    }

    public Boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }

    public String getREPS() {
        return REPS;
    }

    public void setREPS(String REPS) {
        this.REPS = REPS;
    }

    public String getKg() {
        return kg;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }
}
