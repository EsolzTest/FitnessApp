package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 23/6/15.
 */
public class ProfileViewDataType {

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

    public ProfileViewDataType(String id, String user_type, String name, String image, String email, String address, String company, String work_address, String billing_address, String phone, String about) {
        this.id = id;
        this.about = about;
        this.phone = phone;
        this.billing_address = billing_address;
        this.work_address = work_address;
        this.company = company;
        this.address = address;
        this.email = email;
        this.image = image;
        this.name = name;
        this.user_type = user_type;
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
}
