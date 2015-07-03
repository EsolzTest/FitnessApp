package com.esolz.fitnessapp.datatype;

/**
 * Created by ltp on 03/07/15.
 */
public class LoginDataType {
    String siteUserId, username, password;

    public LoginDataType(String siteUserId, String username, String password) {
        this.siteUserId = siteUserId;
        this.username = username;
        this.password = password;
    }

    public String getSiteUserId() {
        return siteUserId;
    }

    public void setSiteUserId(String siteUserId) {
        this.siteUserId = siteUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
