package com.esolz.fitnessapp.datatype;

import java.util.ArrayList;

/**
 * Created by ltp on 14/07/15.
 */
public class ParticularExerciseDetailsDataType {
    String exercise_id, exercise_title, exercise_description, exercise_image, exercise_video, instruction, finished;
    ArrayList<TrainingPerticularExerciseSetsDatatype> trainingPerticularExerciseSetsDatatypeArrayList;

    public ParticularExerciseDetailsDataType(String exercise_id, String exercise_title, String exercise_description, String exercise_image, String exercise_video,
                                             String instruction, String finished, ArrayList<TrainingPerticularExerciseSetsDatatype> trainingPerticularExerciseSetsDatatypeArrayList) {
        this.exercise_id = exercise_id;
        this.exercise_title = exercise_title;
        this.exercise_description = exercise_description;
        this.exercise_image = exercise_image;
        this.exercise_video = exercise_video;
        this.instruction = instruction;
        this.finished = finished;
        this.trainingPerticularExerciseSetsDatatypeArrayList = trainingPerticularExerciseSetsDatatypeArrayList;
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

    public String getExercise_image() {
        return exercise_image;
    }

    public void setExercise_image(String exercise_image) {
        this.exercise_image = exercise_image;
    }

    public String getExercise_video() {
        return exercise_video;
    }

    public void setExercise_video(String exercise_video) {
        this.exercise_video = exercise_video;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public ArrayList<TrainingPerticularExerciseSetsDatatype> getTrainingPerticularExerciseSetsDatatypeArrayList() {
        return trainingPerticularExerciseSetsDatatypeArrayList;
    }

    public void setTrainingPerticularExerciseSetsDatatypeArrayList(ArrayList<TrainingPerticularExerciseSetsDatatype> trainingPerticularExerciseSetsDatatypeArrayList) {
        this.trainingPerticularExerciseSetsDatatypeArrayList = trainingPerticularExerciseSetsDatatypeArrayList;
    }
}
