package com.esolz.fitnessapp.datatype;

public class EventDataType {

	String markedDay, typeEvent;
    boolean isSelected;

	public EventDataType(String markedDay, String typeEvent, boolean isSelected) {
		this.markedDay = markedDay;
		this.typeEvent = typeEvent;
        this.isSelected = isSelected;
	}

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
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
