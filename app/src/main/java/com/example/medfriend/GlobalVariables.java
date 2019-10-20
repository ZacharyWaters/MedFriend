package com.example.medfriend;

import android.app.Application;

public class GlobalVariables extends Application {

    private String currentUserName;
    private String currentUserEmail;
    private String currentUserPassword;
    private String currentUserID;

    public String getCurrentUserName() {
        return currentUserName;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public String getCurrentUserPassword() {
        return currentUserPassword;
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserName(String someVariable) {
        this.currentUserName = someVariable;
    }

    public void setCurrentUserEmail(String someVariable) {
        this.currentUserEmail = someVariable;
    }

    public void setCurrentUserPassword(String someVariable) {
        this.currentUserPassword = someVariable;
    }

    public void setCurrentUserID(String someVariable) {
        this.currentUserID = someVariable;
    }
}
