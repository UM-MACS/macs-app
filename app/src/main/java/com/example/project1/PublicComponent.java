package com.example.project1;

public class PublicComponent {

    //shared preference common
    public static final int PRIVATE_MODE = 0;

    //exercise shared preference
    public static final String EXERCISE_ACCESS = "EXERCISE_ACCESS";
    public static final String EXERCISE_TYPE = "EXERCISE_TYPE";
    public static final String EXERCISE_LIST = "EXERCISE_LIST";
    public static final String DESIRE_TO_BE_REMIND = "DESIRE_TO_BE_REMIND";
    public static final String DESIRE_EXERCISE_DAY = "DESIRE_EXERCISE_DAY";

    //sqlite shared preference
    public static final String SQLITE_ACCESS = "SQLITE_ACCESS";
    public static final String IS_CHAT_CHANNEL_LIST_LOAD = "IS_CHAT_CHANNEL_LIST_LOAD";
    public static final String IS_CONTACT_LIST_LOAD = "IS_CONTACT_LIST_LOAD";

    //user type
    public static final String PATIENT = "Patient";
    public static final String CAREGIVER = "Caregiver";
    public static final String SPECIALIST = "Specialist";
    public static final String ADMIN = "Admin";

    //mysql database API address
    public static final String LOCALHOST = "https://masc-server.herokuapp.com";
    public static final String URL_GET_CHAT_CHANNEL = LOCALHOST + "/getChatChannel/";
    public static final String URL_POST_CHAT_CHANNEL = LOCALHOST + "/postChatChannel/";
    public static final String URL_SPECIALIST_PIC = LOCALHOST + "/getSpecialistPic/";
    public static final String URL_PATIENT_PIC = LOCALHOST + "/getPatientPic/";
    public static final String URL_CAREGIVER_PIC = LOCALHOST + "/getCaregiverPic/";
    public static final String URL_GET_ALL_PATIENT = LOCALHOST + "/getAllPatient/";
    public static final String URL_GET_ALL_CAREGIVER = LOCALHOST + "/getAllCaregiver/";
    public static final String URL_GET_ALL_SPECIALIST = LOCALHOST + "/getAllSpecialist/";
    public static final String API_CALL_STATUS = "success";

    //firebase database reference key
    public static final String FIREBASE_CHAT_BASE = "chat";
    public static final String FIREBASE_CHAT_CHANNEL_TYPING_STATUS = "typingStatus";
    public static final String FIREBASE_CHAT_CHANNEL_CHAT_HISTORY = "chatHistory";
    public static final String FIREBASE_CHAT_HISTORY_CHANNEL_ID = "chatChannelId";
    public static final String FIREBASE_CHAT_HISTORY_MESSAGE_ID = "messageId";
    public static final String FIREBASE_CHAT_HISTORY_MESSAGE_TYPE = "messageType";
    public static final String FIREBASE_CHAT_HISTORY_MEDIA_URL = "mediaUrl";
    public static final String FIREBASE_CHAT_HISTORY_MESSAGE = "message";
    public static final String FIREBASE_CHAT_HISTORY_EMAIL_FROM = "emailFrom";
    public static final String FIREBASE_CHAT_HISTORY_EMAIL_TO = "emailTo";
    public static final String FIREBASE_CHAT_HISTORY_TIMESTAMP = "timestamp";
    public static final String FIREBASE_CHAT_UNREAD_BASE = "unread";

    //TODO
    public static String parseTimestampToString(String timestamp){
        return "xxx ago";
    }
}
