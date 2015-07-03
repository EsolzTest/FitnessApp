package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 23/6/15.
 */
public class CalanderFragmentDairyDataType {

String client_id;
    String client_name;
    String client_image;
    String client_email;
    String client_about;
    String diary_id;
    String diary_heading;
    String dairy_text;

    public CalanderFragmentDairyDataType(String client_id, String client_name, String client_image, String client_email, String client_about, String diary_id, String diary_heading, String dairy_text) {
        this.client_id = client_id;
        this.client_name = client_name;
        this.client_image = client_image;
        this.client_email = client_email;
        this.client_about = client_about;
        this.diary_id = diary_id;
        this.diary_heading = diary_heading;
        this.dairy_text = dairy_text;
    }


    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_image() {
        return client_image;
    }

    public void setClient_image(String client_image) {
        this.client_image = client_image;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }

    public String getClient_about() {
        return client_about;
    }

    public void setClient_about(String client_about) {
        this.client_about = client_about;
    }

    public String getDiary_id() {
        return diary_id;
    }

    public void setDiary_id(String diary_id) {
        this.diary_id = diary_id;
    }

    public String getDiary_heading() {
        return diary_heading;
    }

    public void setDiary_heading(String diary_heading) {
        this.diary_heading = diary_heading;
    }

    public String getDairy_text() {
        return dairy_text;
    }

    public void setDairy_text(String dairy_text) {
        this.dairy_text = dairy_text;
    }
}
