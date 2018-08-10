package com.narmware.doormojovendor;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

/**
 * Created by rohitsavant on 02/08/18.
 */

public class MyApplication extends MultiDexApplication {
    public static final String SERVER_URL="http://www.aaplitopli.com/api/";
    public static final String LOGIN_USER=SERVER_URL+"login.php";

    public static void mt(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
