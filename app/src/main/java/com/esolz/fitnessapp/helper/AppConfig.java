package com.esolz.fitnessapp.helper;

import android.content.SharedPreferences;

import com.esolz.fitnessapp.datatype.LoginDataType;

import java.util.ArrayList;

public class AppConfig {
	
	public static SharedPreferences loginPreferences;
	public static boolean isRemember = true;
	public static String strRemember = "N";

	public static ArrayList<LoginDataType> loginDataTypeArrayList = new ArrayList<LoginDataType>();
	public  static LoginDataType loginDataType;

}
