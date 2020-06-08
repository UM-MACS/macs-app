package com.example.project1.chat.component;

public class CurrentChatUser {

    private static CurrentChatUser myInstance;
    private String currentNRIC = "";

    public static CurrentChatUser getInstance() {
        if(myInstance == null){
            myInstance = new CurrentChatUser();
        }
        return myInstance;
    }

    private CurrentChatUser() {
    }

    public String getCurrentNRIC() {
        return currentNRIC;
    }

    public void setCurrentNRIC(String currentNRIC) {
        this.currentNRIC = currentNRIC;
    }

}
