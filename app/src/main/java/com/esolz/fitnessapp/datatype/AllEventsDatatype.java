package com.esolz.fitnessapp.datatype;

/**
 * Created by ltp on 03/07/15.
 */
public class AllEventsDatatype {
    String total_meal, total_appointment, diary_text, total_training_exercises, total_training_exercise_finished, total_training_programs, total_training_programs_finished;

    public AllEventsDatatype(String total_meal, String total_appointment, String diary_text, String total_training_exercises, String total_training_exercise_finished, String total_training_programs, String total_training_programs_finished) {
        this.total_meal = total_meal;
        this.total_appointment = total_appointment;
        this.diary_text = diary_text;
        this.total_training_exercises = total_training_exercises;
        this.total_training_exercise_finished = total_training_exercise_finished;
        this.total_training_programs = total_training_programs;
        this.total_training_programs_finished = total_training_programs_finished;
    }

    public String getTotal_meal() {
        return total_meal;
    }

    public void setTotal_meal(String total_meal) {
        this.total_meal = total_meal;
    }

    public String getTotal_appointment() {
        return total_appointment;
    }

    public void setTotal_appointment(String total_appointment) {
        this.total_appointment = total_appointment;
    }

    public String getDiary_text() {
        return diary_text;
    }

    public void setDiary_text(String diary_text) {
        this.diary_text = diary_text;
    }

    public String getTotal_training_exercises() {
        return total_training_exercises;
    }

    public void setTotal_training_exercises(String total_training_exercises) {
        this.total_training_exercises = total_training_exercises;
    }

    public String getTotal_training_exercise_finished() {
        return total_training_exercise_finished;
    }

    public void setTotal_training_exercise_finished(String total_training_exercise_finished) {
        this.total_training_exercise_finished = total_training_exercise_finished;
    }

    public String getTotal_training_programs() {
        return total_training_programs;
    }

    public void setTotal_training_programs(String total_training_programs) {
        this.total_training_programs = total_training_programs;
    }

    public String getTotal_training_programs_finished() {
        return total_training_programs_finished;
    }

    public void setTotal_training_programs_finished(String total_training_programs_finished) {
        this.total_training_programs_finished = total_training_programs_finished;
    }
}
