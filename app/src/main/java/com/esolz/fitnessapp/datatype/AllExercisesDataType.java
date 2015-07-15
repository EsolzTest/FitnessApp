package com.esolz.fitnessapp.datatype;

import java.util.ArrayList;

/**
 * Created by ltp on 14/07/15.
 */
public class AllExercisesDataType {
    String user_program_id, exercise_id, exercise_title, instruction;
    ArrayList<ExerciseSetsDataype> exerciseSetsDataypeArrayList;

    public AllExercisesDataType(String user_program_id, String exercise_id, String exercise_title,
                                String instruction, ArrayList<ExerciseSetsDataype> exerciseSetsDataypeArrayList) {
        this.user_program_id = user_program_id;
        this.exercise_id = exercise_id;
        this.exercise_title = exercise_title;
        this.instruction = instruction;
        this.exerciseSetsDataypeArrayList = exerciseSetsDataypeArrayList;
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

    public ArrayList<ExerciseSetsDataype> getExerciseSetsDataypeArrayList() {
        return exerciseSetsDataypeArrayList;
    }

    public void setExerciseSetsDataypeArrayList(ArrayList<ExerciseSetsDataype> exerciseSetsDataypeArrayList) {
        this.exerciseSetsDataypeArrayList = exerciseSetsDataypeArrayList;
    }
}
