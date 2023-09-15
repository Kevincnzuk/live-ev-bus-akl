package com.github.kevincnzuk.aklliveevbus;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class SPHelper {

    private static final String TAG = "SharedPreferencesHelper";
    public static final String PRIMARY_KEY = "primary_key";
    public static final String SECONDARY_KEY = "secondary_key";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Context context;

    private static SPHelper sharedPreferencesHelper;
    private void SharedPreferencesHelper() {
    }

    public static SPHelper getInstance(Context context){
        if (sharedPreferencesHelper == null){
            sharedPreferencesHelper = new SPHelper();
            sharedPreferencesHelper.context = context;
            sharedPreferencesHelper.pref =  context.getSharedPreferences("app", MODE_PRIVATE);
            sharedPreferencesHelper.editor = sharedPreferencesHelper.pref.edit();
        }
        Log.d(TAG, "getInstance");
        return sharedPreferencesHelper;
    }

    /**
     * Save a String to pref.
     * @param name
     * @param content
     */
    public void save(String name, String content) {
        editor.putString(name, content);
        editor.commit();
        Log.d(TAG, "save: String: " + name);
    }

    /**
     * Save a boolean to pref.
     * @param name
     * @param content
     */
    public void save(String name, boolean content) {
        editor.putBoolean(name, content);
        editor.commit();
        Log.d(TAG, "save: boolean: " + name);
    }

    /**
     * Save a int to pref.
     * @param name
     * @param content
     */
    public void save(String name, int content) {
        editor.putInt(name, content);
        editor.commit();
        Log.d(TAG, "save: int: " + name);
    }

    /**
     * Get a String from pref.
     * @param name
     * @return
     */
    public String get(String name, String defValue) {
        Log.d(TAG, "get: String: " + name);
        return pref.getString(name, defValue);
    }

    /**
     * Get a boolean from pref.
     * @param name
     * @return
     */
    public boolean get(String name, boolean defValue) {
        Log.d(TAG, "get: boolean: " + name);
        return pref.getBoolean(name, defValue);
    }

    /**
     * Get a int from pref.
     * @param name
     * @return
     */
    public int get(String name, int defValue) {
        Log.d(TAG, "get: int: " + name);
        return pref.getInt(name, defValue);
    }

}
