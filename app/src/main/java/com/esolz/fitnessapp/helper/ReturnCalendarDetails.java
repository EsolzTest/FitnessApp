package com.esolz.fitnessapp.helper;

import java.util.Calendar;
import java.util.Locale;

public class ReturnCalendarDetails {
	Calendar calendar = Calendar.getInstance(Locale.getDefault());

	// static int monthLength;

	public static boolean isLeapYear(int year) {
		if (year > 1582) {
			if (year % 4 != 0) {
				return false;
			} else if (year % 100 == 0 && year % 400 != 0) {
				return false;
			} else if (year % 400 == 0) {
				return true;
			} else {
				return true;
			}
		}
		return false;
	}

	public static int getMonthLenth(int month, int year) {
		int monthLength;
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			monthLength = 30;
		} else if (month == 2) {
			if (isLeapYear(year)) {
				monthLength = 28 + 1;
			} else {
				monthLength = 28;
			}
		} else {
			monthLength = 31;
		}
		return monthLength;
	}

	public static int getPosition(String day) {
		int position = 0;
		if (day.equalsIgnoreCase("sun")) {
			position = 0;
		} else if (day.equalsIgnoreCase("mon")) {
			position = 1;
		} else if (day.equalsIgnoreCase("tue")) {
			position = 2;
		} else if (day.equalsIgnoreCase("wed")) {
			position = 3;
		} else if (day.equalsIgnoreCase("thu")) {
			position = 4;
		} else if (day.equalsIgnoreCase("fri")) {
			position = 5;
		} else if (day.equalsIgnoreCase("sat")) {
			position = 6;
		} else {
			position = -1;
		}
		return position;
	}

	public static int getCurrentMonth(String month) {
		int monthMosition = 0;
		if (month.equalsIgnoreCase("jan")) {
			monthMosition = 1;
		} else if (month.equalsIgnoreCase("feb")) {
			monthMosition = 2;
		} else if (month.equalsIgnoreCase("mar")) {
			monthMosition = 3;
		} else if (month.equalsIgnoreCase("apr")) {
			monthMosition = 4;
		} else if (month.equalsIgnoreCase("may")) {
			monthMosition = 5;
		} else if (month.equalsIgnoreCase("jun")) {
			monthMosition = 6;
		} else if (month.equalsIgnoreCase("jul")) {
			monthMosition = 7;
		} else if (month.equalsIgnoreCase("aug")) {
			monthMosition = 8;
		} else if (month.equalsIgnoreCase("sep")) {
			monthMosition = 9;
		} else if (month.equalsIgnoreCase("oct")) {
			monthMosition = 10;
		} else if (month.equalsIgnoreCase("nov")) {
			monthMosition = 11;
		} else if (month.equalsIgnoreCase("dec")) {
			monthMosition = 12;
		} else {
			monthMosition = -1;
		}
		return monthMosition;
	}

}
