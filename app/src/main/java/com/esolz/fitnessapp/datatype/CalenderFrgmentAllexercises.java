package com.esolz.fitnessapp.datatype;

import java.util.LinkedList;

/**
 * Created by su on 19/6/15.
 */
public class CalenderFrgmentAllexercises {

    String user_program_id, exercise_id, exercise_title, instruction;
    LinkedList<CalenderPageExSets> exercise_sets;

    public CalenderFrgmentAllexercises(String user_program_id, String exercise_id, String exercise_title, String instruction, LinkedList<CalenderPageExSets> allEx) {
        this.user_program_id = user_program_id;
        this.exercise_id = exercise_id;
        this.exercise_title = exercise_title;
        this.instruction = instruction;
        this.exercise_sets = allEx;
    }


    public String getUser_program_id() {
        return user_program_id;
    }

    public void setUser_program_id(String user_program_id) {
        this.user_program_id = user_program_id;
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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public LinkedList<CalenderPageExSets> getExercise_sets() {
        return exercise_sets;
    }

    public void setExercise_sets(LinkedList<CalenderPageExSets> exercise_sets) {
        this.exercise_sets = exercise_sets;
    }
}
