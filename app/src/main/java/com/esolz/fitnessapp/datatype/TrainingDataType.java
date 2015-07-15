package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 23/6/15.
 */
public class TrainingDataType {

    String reps;
    String kg;
    String exercise_id;
    String exercise_title;
    String exercise_description;
    String instruction;

    public TrainingDataType(String reps, String kg, String exercise_id, String exercise_title,
                            String exercise_description, String instruction) {
        this.reps = reps;
        this.kg = kg;
        this.exercise_id = exercise_id;
        this.exercise_title = exercise_title;
        this.exercise_description = exercise_description;
        this.instruction = instruction;
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

    public String getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(String exercise_id) {
        this.exercise_id = exercise_id;
    }

    public String getExercise_title() {
        return exercise_title;
    }

    public void setExercise_title(String exercise_title) {
        this.exercise_title = exercise_title;
    }

    public String getExercise_description() {
        return exercise_description;
    }

    public void setExercise_description(String exercise_description) {
        this.exercise_description = exercise_description;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
