package com.esolz.fitnessapp.datatype;

/**
 * Created by ltp on 13/07/15.
 */
public class UserRespectiveMSGDatatype {
   String  msgID, sent_to, sent_by, message, read_status, send_time, sender, receiver, sender_image, receiver_image, status;
    Boolean isSender;
    int startPosition;

    public UserRespectiveMSGDatatype(Boolean isSender, String msgID, String sent_to, String sent_by, String message,
                                     String read_status, String send_time, String sender, String receiver, String sender_image,
                                     String receiver_image, String status, int startPosition) {
        this.isSender = isSender;
        this.msgID = msgID;
        this.sent_to = sent_to;
        this.sent_by = sent_by;
        this.message = message;
        this.read_status = read_status;
        this.send_time = send_time;
        this.sender = sender;
        this.receiver = receiver;
        this.sender_image = sender_image;
        this.receiver_image = receiver_image;
        this.status = status;
        this.startPosition = startPosition;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public Boolean getIsSender() {
        return isSender;
    }

    public void setIsSender(Boolean isSender) {
        this.isSender = isSender;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getSent_to() {
        return sent_to;
    }

    public void setSent_to(String sent_to) {
        this.sent_to = sent_to;
    }

    public String getSent_by() {
        return sent_by;
    }

    public void setSent_by(String sent_by) {
        this.sent_by = sent_by;
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