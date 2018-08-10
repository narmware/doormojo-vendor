package com.narmware.doormojovendor.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by comp16 on 12/19/2017.
 */

public class SharedPreferencesHelper {

    private static final String IS_LOGIN="login";
    private static final String USER_NAME="name";
    private static final String USER_MAIL="mail";
    private static final String USER_ID="id";
    private static final String USER_MOBILE1="m1";
    private static final String USER_MOBILE2="m2";
    private static final String USER_MOBILE3="m3";
    private static final String USER_MOBILE4="m4";


    public static void setUserName(String name, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_NAME,name);
        edit.commit();
    }

    public static String getUserName(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String name=pref.getString(USER_NAME,null);
        return name;
    }


    public static void setUserMail(String mail, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_MAIL,mail);
        edit.commit();
    }

    public static String getUserMail(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String mail = pref.getString(USER_MAIL,null);
        return mail;
    }


    public static void setUserId(String id, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_ID,id);
        edit.commit();
    }

    public static String getUserId(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String id=pref.getString(USER_ID,null);
        return id;
    }


    public static void setUserM1(String m, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_MOBILE1,m);
        edit.commit();
    }
    public static String getUserM1(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String m=pref.getString(USER_MOBILE1,null);
        return m;
    }


    public static void setUserM2(String m, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_MOBILE2,m);
        edit.commit();
    }
    public static String getUserM2(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String m=pref.getString(USER_MOBILE2,null);
        return m;
    }


    public static void setUserM3(String m, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_MOBILE3,m);
        edit.commit();
    }
    public static String getUserM3(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String m=pref.getString(USER_MOBILE3,null);
        return m;
    }


    public static void setUserM4(String m, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_MOBILE4,m);
        edit.commit();
    }
    public static String getUserM4(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String m=pref.getString(USER_MOBILE4,null);
        return m;
    }


    public static void setIsLogin(boolean login, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_LOGIN,login);
        edit.commit();
    }

    public static boolean getIsLogin(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean login=pref.getBoolean(IS_LOGIN,false);
        return login;
    }

}

