package com.pgmacdesign.demolinktogae.misc;

import android.app.Application;
import android.content.Context;

/**
 * Created by pmacdowell on 2015/12/15.
 */
public class MyApplication  extends Application {

    private static MyApplication sInstance;

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    /*
    //Initialize a database here if need be
    public synchronized static DBMovies getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new DBMovies(getAppContext());
        }
        return mDatabase;
    }
    */

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}