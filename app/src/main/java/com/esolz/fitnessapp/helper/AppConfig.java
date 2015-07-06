package com.esolz.fitnessapp.helper;

import android.content.SharedPreferences;

import com.esolz.fitnessapp.datatype.AppointDataType;
import com.esolz.fitnessapp.datatype.DiaryDataType;
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

}
