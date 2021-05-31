package com.example.project1.login.component;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.annotation.StringDef;

import com.example.project1.chat.service.NotificationService;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.login.LoginActivity;
import com.example.project1.emotionAssessment.EmotionButtonAssessmentActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Locale;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String NRIC = "NRIC";
    public static final String TYPE = "TYPE";

    /* Language Declare */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ENGLISH, MALAY, CHINESE})
    public @interface LocaleDef {
        String[] SUPPORTED_LOCALES = { ENGLISH, MALAY, CHINESE};
    }

    public static final String ENGLISH = "en";
    public static final String MALAY = "ms";
    public static final String CHINESE = "zh";
    private static final String LANGUAGE_KEY = "language_key";

    /* End of Language Declare */


    /* Check Login */
    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String nric, String type){

        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(NRIC, nric);
        editor.putString(TYPE, type);
        editor.apply();

    }

    public boolean isLogin(){

        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if (!this.isLogin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((EmotionAssessmentActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){

        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(NRIC, sharedPreferences.getString(NRIC, null));
        user.put(TYPE, sharedPreferences.getString(TYPE, null));

        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, NotificationService.class));
        } else {
            context.stopService(new Intent(context, NotificationService.class));
        }
//        context.startForegroundService(new Intent(context, NotificationService.class));
//        context.stopService(new Intent(context, NotificationService.class));
    }

    public int isFirstTimeUser(){
        SharedPreferences sp = context.getSharedPreferences("FIRST_TIME_USER", PRIVATE_MODE);
        SharedPreferences.Editor ed = sp.edit();

        // 0 is first time, 1 is not first time
        int n = sp.getInt("IS_FIRST_TIME_USER",0);

        if(n == 0){
            ed.putInt("IS_FIRST_TIME_USER", 1);
            ed.apply();
        }
        return n;
    }

    public int isFirstTimeCaregiver(){
        SharedPreferences sp = context.getSharedPreferences("FIRST_TIME_CAREGIVER", PRIVATE_MODE);
        SharedPreferences.Editor ed = sp.edit();

        // 0 is first time, 1 is not first time
        int n = sp.getInt("IS_FIRST_TIME_CAREGIVER",0);

        if(n == 0){
            ed.putInt("IS_FIRST_TIME_CAREGIVER", 1);
            ed.apply();
        }
        return n;
    }

    public int isFirstTimeSpecialist(){
        SharedPreferences sp = context.getSharedPreferences("FIRST_TIME_SPECIALIST", PRIVATE_MODE);
        SharedPreferences.Editor ed = sp.edit();

        // 0 is first time, 1 is not first time
        int n = sp.getInt("IS_FIRST_TIME_SPECIALIST",0);

        if(n == 0){
            ed.putInt("IS_FIRST_TIME_SPECIALIST", 1);
            ed.apply();
        }
        return n;
    }

    /* End of Check Login*/

    /* Check Language*/

    /**
     * set current pref locale
     */
    public static Context setLocale(Context mContext) {
        return updateResources(mContext, getLanguagePref(mContext));
    }

    /**
     * Set new Locale with context
     */
    public static Context setNewLocale(Context mContext, @LocaleDef String language) {
        setLanguagePref(mContext, language);
        return updateResources(mContext, language);
    }

    /**
     * get current locale
     */
    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;
    }

    /**
     * Get saved Locale from SharedPreferences
     *
     * @param mContext current context
     * @return current locale key by default return english locale
     */
    public static String getLanguagePref(Context mContext) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return mPreferences.getString(LANGUAGE_KEY, ENGLISH);
    }

    /**
     * set pref key
     */
    private static void setLanguagePref(Context mContext, String localeKey) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mPreferences.edit().putString(LANGUAGE_KEY, localeKey).apply();
    }

    /**
     * update resource
     */
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

}