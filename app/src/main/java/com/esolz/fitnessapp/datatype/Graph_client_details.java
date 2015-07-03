package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 18/6/15.
 */
public class Graph_client_details {
    String id;
    String user_type;
    String name;
    String image;
    String email;
    String address;
    String company;
    String work_address;
    String billing_address;
    String phone;
    String about;
    String date_of_birth;
    String height;
    String weight;


    public Graph_client_details(String id, String user_type, String name, String image, String email, String address, String company, String work_address, String billing_address, String phone, String about, String date_of_birth, String weight, String height) {
        this.id = id;
        this.user_type = user_type;
        this.name = name;
        this.image = image;
        this.email = email;
        this.address = address;
        this.company = company;
        this.work_address = work_address;
        this.billing_address = billing_address;
        this.phone = phone;
        this.about = about;
        this.date_of_birth = date_of_birth;
        this.weight = weight;
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWork_address() {
        return work_address;
    }

    public void setWork_address(String work_address) {
        this.work_address = work_address;
    }

    public String getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(String billing_address) {
        this.billing_address = billing_address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
