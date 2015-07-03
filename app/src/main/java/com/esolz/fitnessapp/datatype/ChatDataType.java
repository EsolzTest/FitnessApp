package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 18/6/15.
 */
public class ChatDataType {
    String id;
    String send_to;
    String send_by;
    String message;
    String read_status;
    String send_time;
    String sender;
    String receiver;
    String sender_image;
    String receiver_image;
    String status;









    public boolean is_added() {
        return is_added;
    }

    public void setIs_added(boolean is_added) {
        this.is_added = is_added;
    }

    boolean is_added;






    public ChatDataType(String id, String send_to, String send_by, String message, String read_status, String send_time, String sender, String receiver, String sender_image, String receiver_image, String status) {
        this.id = id;
        this.send_to = send_to;
        this.send_by = send_by;
        this.message = message;
        this.read_status = read_status;
        this.send_time = send_time;
        this.sender = sender;
        this.receiver = receiver;
        this.sender_image = sender_image;
        this.receiver_image = receiver_image;
        this.status = status;
       // this.json_arr_lv = json_arr_lv;

    }














    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSend_to() {
        return send_to;
    }

    public void setSend_to(String send_to) {
        this.send_to = send_to;
    }

    public String getSend_by() {
        return send_by;
    }

    public void setSend_by(String send_by) {
        this.send_by = send_by;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender_image() {
        return sender_image;
    }

    public void setSender_image(String sender_image) {
        this.sender_image = sender_image;
    }

    public String getReceiver_image() {
        return receiver_image;
    }

    public void setReceiver_image(String receiver_image) {
        this.receiver_image = receiver_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
