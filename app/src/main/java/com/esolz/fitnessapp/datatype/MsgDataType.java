package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 18/6/15.
 */
public class MsgDataType {

    String user_id;
    String last_send_time;
    String user_name;
    String user_image;
    String last_message;

    public MsgDataType(String user_id, String last_send_time, String user_name, String user_image, String last_message) {
        this.user_id = user_id;
        this.last_send_time = last_send_time;
        this.user_name = user_name;
        this.user_image = user_image;
        this.last_message = last_message;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLast_send_time() {
        return last_send_time;
    }

    public void setLast_send_time(String last_send_time) {
        this.last_send_time = last_send_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }



}
