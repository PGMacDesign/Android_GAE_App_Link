package com.pgmacdesign.demolinktogae.misc;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by pmacdowell on 12/18/2015.
 */
public class SharedPrefs {

    private static SharedPreferences.Editor edit1;
    private static SharedPreferences prefs1;
    private static final String prefsName = "DemoLinkToGAE";
    /*
    All the methods below are static save methods. First param is the key and the second is the
    value. There are multiple overloaded types depending on type passed in.
    */

    public static void save(String valueKey, String value) {
        init();
        edit1.putString(valueKey, value);
        edit1.commit();
    }

    public static void save(String valueKey, int value) {
        //SharedPreferences.Editor edit = getEditor();
        init();
        edit1.putInt(valueKey, value);
        edit1.commit();
    }

    public static void save(String valueKey, boolean value) {
        init();
        edit1.putBoolean(valueKey, value);
        edit1.commit();
    }

    public static void save(String valueKey, long value) {
        init();
        edit1.putLong(valueKey, value);
        edit1.commit();
    }

    public static void save(String valueKey, double value) {
        init();
        edit1.putLong(valueKey, Double.doubleToRawLongBits(value));
        edit1.commit();
    }

    public static void save(String valueKey, Set<String> values) {
        init();
        edit1.putStringSet(valueKey, values);
        edit1.commit();
    }

    public static void save(String valueKey, String[] values) {
        init();
        for(int i = 0 ; i < values.length; i++){
            edit1.putString(valueKey + "-" + i, values[i]);
        }
        edit1.commit();
    }

    public static void save(String valueKey, int[] values) {
        init();
        for(int i = 0 ; i < values.length; i++){
            edit1.putInt(valueKey + "-" + i, values[i]);
        }
        edit1.commit();
    }

    public static void save(String valueKey, long[] values) {
        init();
        for(int i = 0 ; i < values.length; i++){
            edit1.putLong(valueKey + "-" + i, values[i]);
        }
        edit1.commit();
    }

    public static void save(String valueKey, double[] values) {
        init();
        for(int i = 0 ; i < values.length; i++){
            edit1.putLong(valueKey + "-" + i, Double.doubleToRawLongBits(values[i]));
        }
        edit1.commit();
    }

    public static void save(String valueKey, boolean[] values) {
        SharedPreferences.Editor edit = getEditor();
        for(int i = 0 ; i < values.length; i++){
            edit.putBoolean(valueKey + "-" + i, values[i]);
        }
        edit.commit();
    }



    //
    //Get methods
    //

    public static String getString(String valueKey, String defaultValue) {
        init();
        return prefs1.getString(valueKey, defaultValue);
    }

    public static int getInt(String valueKey, int defaultValue) {
        init();
        return prefs1.getInt(valueKey, defaultValue);
    }

    public static boolean getBoolean(String valueKey, boolean defaultValue) {
        init();
        return prefs1.getBoolean(valueKey, defaultValue);
    }

    public static long getLong(String valueKey, long defaultValue) {
        init();
        return prefs1.getLong(valueKey, defaultValue);
    }

    public static double getDouble(String valueKey, double defaultValue) {
        init();
        return Double.longBitsToDouble(prefs1.getLong(valueKey, Double.doubleToLongBits(defaultValue)));
    }

    public static Set<String> getSet(String valueKey, Set<String> defaultValues) {
        init();
        return prefs1.getStringSet(valueKey, defaultValues);
    }

    public static SharedPreferences getPrefs(){
        prefs1 = MyApplication.getAppContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        return prefs1;
    }

    public static SharedPreferences.Editor getEditor(){
        SharedPreferences prefs1 = getPrefs();
        if(edit1 == null){
            edit1 = prefs1.edit();
        }
        return edit1;
    }

    public static void init(){
        if(prefs1 == null){
            getPrefs();
        }
        if(edit1 == null){
            edit1 = prefs1.edit();
        }
    }

    public static void clearPref(String key){
        SharedPreferences.Editor edit = getEditor();
        edit.remove(key);
        edit.commit();
    }

}
