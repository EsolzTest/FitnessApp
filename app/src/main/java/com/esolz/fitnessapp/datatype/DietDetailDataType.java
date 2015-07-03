package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 24/6/15.
 */
public class DietDetailDataType {

    String meal_title;
    String meal_image;
    String meal_description;

    public DietDetailDataType(String meal_title, String meal_image, String meal_description) {
        this.meal_title = meal_title;
        this.meal_image = meal_image;
        this.meal_description = meal_description;
    }


    public String getMeal_title() {
        return meal_title;
    }

    public void setMeal_title(String meal_title) {
        this.meal_title = meal_title;
    }

    public String getMeal_image() {
        return meal_image;
    }

    public void setMeal_image(String meal_image) {
        this.meal_image = meal_image;
    }

    public String getMeal_description() {
        return meal_description;
    }

    public void setMeal_description(String meal_description) {
        this.meal_description = meal_description;
    }
}
