package com.example.project1;

public class PublicComponent {

    public static final int PRIVATE_MODE = 0;
    public static final String EXERCISE_ACCESS = "EXERCISE_ACCESS";
    public static final String EXERCISE_TYPE = "EXERCISE_TYPE";
    public static final String EXERCISE_LIST = "EXERCISE_LIST";
    public static final String DESIRE_TO_BE_REMIND = "DESIRE_TO_BE_REMIND";
    public static final String DESIRE_EXERCISE_DAY = "DESIRE_EXERCISE_DAY";

    public static final String PATIENT = "Patient";
    public static final String CAREGIVER = "Caregiver";
    public static final String SPECIALIST = "Specialist";
    public static final String DOCTOR = "Doctor";

    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String NRIC = "nric";
    public static final String CONTACT_NO = "contactNo";
    public static final String AGE = "age";
    public static final String TYPE = "type";
    public static final String ID = "id";

    public static final String localhost = "https://masc-server.herokuapp.com";
    public static final String SESSION_URL = localhost+"/postExercise/";
    public static final String DETAILS_URL = localhost+"/postExercisedetails";
    public static final String GET_ALL_PATIENT_URL = localhost+"/getAllPatient/";
    public static final String GET_ALL_CAREGIVER_URL = localhost+"/getAllCaregiver/";
    public static final String GET_ALL_SPECIALIST_URL = localhost+"/getAllSpecialist/";
    public static final String DELETE_PATIENT_URL = localhost+"/deletePatient/";
    public static final String DELETE_CAREGIVER_URL = localhost+"/deleteCaregiver/";
    public static final String DELETE_SPECIALIST_URL = localhost+"/deleteSpecialist/";
}
