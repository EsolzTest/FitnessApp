package com.esolz.fitnessapp.datatype;

/**
 * Created by ltp on 19/08/15.
 */
public class ClickDateDataType {
    String day, month, year;
    boolean isClicked;

    public ClickDateDataType(String day, String month, String year, boolean isClicked) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.isClicked = isClicked;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }
}
