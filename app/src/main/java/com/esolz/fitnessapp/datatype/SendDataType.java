package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 24/6/15.
 */
public class SendDataType {

    String id;
    String sender;
    String receiver;
    String sender_image;
    String receiver_image;
    String message;
    String send_time;
    String status;

    public SendDataType(String id, String sender, String receiver, String sender_image, String receiver_image, String message, String send_time, String status) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.sender_image = sender_image;
        this.receiver_image = receiver_image;
        this.message = message;
        this.send_time = send_time;
        this.status = status;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    ;
}
