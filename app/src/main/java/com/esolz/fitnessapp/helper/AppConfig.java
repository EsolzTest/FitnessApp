package com.esolz.fitnessapp.helper;

import android.content.SharedPreferences;

import com.esolz.fitnessapp.datatype.AllExercisesDataType;
import com.esolz.fitnessapp.datatype.AppointDataType;
import com.esolz.fitnessapp.datatype.DiaryDataType;
import com.esolz.fitnessapp.datatype.ExerciseSetsDataype;
import com.esolz.fitnessapp.datatype.GraphDetailsDataType;
import com.esolz.fitnessapp.datatype.GraphDetailsPointDataType;
import com.esolz.fitnessapp.datatype.LoginDataType;
import com.esolz.fitnessapp.datatype.MealDateDataType;
import com.esolz.fitnessapp.datatype.ProgramDateDataType;

import java.util.ArrayList;

public class AppConfig {

    public static String HOST = "http://esolz.co.in/lab6/ptplanner/";

    public static LoginDataType loginDatatype;

    public static SharedPreferences loginPreferences;
    public static boolean isRemember = true;
    public static String strRemember = "N";

    public static String changeDate = "";

    public static ArrayList<AppointDataType> appointmentArrayList = new ArrayList<AppointDataType>();
    public static ArrayList<ProgramDateDataType> programArrayList = new ArrayList<ProgramDateDataType>();
    public static ArrayList<MealDateDataType> mealArrayList = new ArrayList<MealDateDataType>();
    public static ArrayList<DiaryDataType> diaryArrayList = new ArrayList<DiaryDataType>();

    public static String PT_NAME = "";
    public static String PT_ID = "";


    public static ArrayList<AllExercisesDataType> allExercisesDataTypeArrayList/* = new ArrayList<AllExercisesDataType>()*/;
    public static AllExercisesDataType allExercisesDataType;
    public static ArrayList<ExerciseSetsDataype> exerciseSetsDataypeArrayList/* = new ArrayList<ExerciseSetsDataype>()*/;
    public static ExerciseSetsDataype exerciseSetsDataype;

    public static ArrayList<GraphDetailsDataType> graphDetailsDataTypeArrayList;
    public static GraphDetailsDataType graphDetailsDataType;
    public static ArrayList<GraphDetailsPointDataType> graphDetailsPointDataTypeArrayList;
    public static GraphDetailsPointDataType graphDetailsPointDataType;

    public static String appRegId = "";

}
