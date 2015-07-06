package com.esolz.fitnessapp.datatype;

public class EventDataType {

	String markedDay, typeEvent;

	public EventDataType(String markedDay, String typeEvent) {
		this.markedDay = markedDay;
		this.typeEvent = typeEvent;
	}

	public String getMarkedDay() {
		return markedDay;
	}

	public void setMarkedDay(String markedDay) {
		this.markedDay = markedDay;
	}

	public String getTypeEvent() {
		return typeEvent;
	}

	public void setTypeEvent(String typeEvent) {
		this.typeEvent = typeEvent;
	}

}
