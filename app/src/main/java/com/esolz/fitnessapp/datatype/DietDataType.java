package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 23/6/15.
 */
public class DietDataType {
    String meal_id;
    String custom_meal_id;
    String meal_title;
    String meal_description;
    String meal_image;

    public DietDataType(String meal_id, String meal_image, String meal_description, String meal_title, String custom_meal_id) {
        this.meal_id = meal_id;
        this.meal_image = meal_image;
        this.meal_description = meal_description;
        this.meal_title = meal_title;
        this.custom_meal_id = custom_meal_id;
    }

    public String getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(String meal_id) {
        this.meal_id = meal_id;
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

    public String getMeal_title() {
        return meal_title;
    }

    public void setMeal_title(String meal_title) {
        this.meal_title = meal_title;
    }

    public String getCustom_meal_id() {
        return custom_meal_id;
    }

    public void setCustom_meal_id(String custom_meal_id) {
        this.custom_meal_id = custom_meal_id;
    }
}
