package com.esolz.fitnessapp.datatype;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by su on 19/6/15.
 */
public class CalenderFragmentDatatype {

    int total_meal, total_appointment, total_training_exercises, total_training_exercise_finished, total_training_programs, total_training_programs_finished;
    String diary_text;
    LinkedList<CalenderFrgmentAllexercises> all_exercises;


    public CalenderFragmentDatatype(int total_meal, int total_appointment, int total_training_exercises, int total_training_exercise_finished, int total_training_programs, int total_training_programs_finished, String diary_text, LinkedList<CalenderFrgmentAllexercises> all_exercises) {
        this.total_meal = total_meal;
        this.total_appointment = total_appointment;
        this.total_training_exercises = total_training_exercises;
        this.total_training_exercise_finished = total_training_exercise_finished;
        this.total_training_programs = total_training_programs;
        this.total_training_programs_finished = total_training_programs_finished;
        this.diary_text = diary_text;
        this.all_exercises = all_exercises;
    }

    public CalenderFragmentDatatype() {

    }


    public int getTotal_meal() {
        return total_meal;
    }

    public void setTotal_meal(int total_meal) {
        this.total_meal = total_meal;
    }

    public int getTotal_training_exercises() {
        return total_training_exercises;
    }

    public void setTotal_training_exercises(int total_training_exercises) {
        this.total_training_exercises = total_training_exercises;
    }

    public int getTotal_appointment() {
        return total_appointment;
    }

    public void setTotal_appointment(int total_appointment) {
        this.total_appointment = total_appointment;
    }

    public int getTotal_training_exercise_finished() {
        return total_training_exercise_finished;
    }

    public void setTotal_training_exercise_finished(int total_training_exercise_finished) {
        this.total_training_exercise_finished = total_training_exercise_finished;
    }

    public int getTotal_training_programs() {
        return total_training_programs;
    }

    public void setTotal_training_programs(int total_training_programs) {
        this.total_training_programs = total_training_programs;
    }

    public int getTotal_training_programs_finished() {
        return total_training_programs_finished;
    }

    public void setTotal_training_programs_finished(int total_training_programs_finished) {
        this.total_training_programs_finished = total_training_programs_finished;
    }

    public String getDiary_text() {
        return diary_text;
    }

    public void setDiary_text(String diary_text) {
        this.diary_text = diary_text;
    }

    public LinkedList<CalenderFrgmentAllexercises> getAll_exercises() {
        return all_exercises;
    }

    public void setAll_exercises(LinkedList<CalenderFrgmentAllexercises> all_exercises) {
        this.all_exercises = all_exercises;
    }
}
