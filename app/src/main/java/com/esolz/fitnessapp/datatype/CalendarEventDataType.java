package com.esolz.fitnessapp.datatype;

public class CalendarEventDataType {
	String program_date, meal_date, diary_date, appointment_date;

	/*public CalendarEventDataType(String program_date, String meal_date,
			String diary_date, String appointment_date) {
		this.program_date = program_date;
		this.meal_date = meal_date;
		this.diary_date = diary_date;
		this.appointment_date = appointment_date;
	}*/

	public String getProgram_date() {
		return program_date;
	}

	public void setProgram_date(String program_date) {
		this.program_date = program_date;
	}

	public String getMeal_date() {
		return meal_date;
	}

	public void setMeal_date(String meal_date) {
		this.meal_date = meal_date;
	}

	public String getDiary_date() {
		return diary_date;
	}

	public void setDiary_date(String diary_date) {
		this.diary_date = diary_date;
	}

	public String getAppointment_date() {
		return appointment_date;
	}

	public void setAppointment_date(String appointment_date) {
		this.appointment_date = appointment_date;
	}

}
