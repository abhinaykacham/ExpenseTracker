package com.expensetracker;

public class User {
    String mEmail;
    String mUsername;
    String mPwd;
    String mPhone;

    public User(String email, String username, String pwd, String phone) {
        mEmail = email;
        mUsername = username;
        mPwd = pwd;
        mPhone = phone;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setPwd(String pwd) {
        mPwd = pwd;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }


    public String getEmail() {
        return mEmail;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPwd() {
        return mPwd;
    }

    public String getPhone() {
        return mPhone;
    }


}
