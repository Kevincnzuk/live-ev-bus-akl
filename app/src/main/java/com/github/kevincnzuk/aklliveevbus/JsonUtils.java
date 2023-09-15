package com.github.kevincnzuk.aklliveevbus;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    private static final String TAG = "JsonUtils";

    /**
     * Check if name exist in father, if yes, return the value (string), if not, return "null".
     * @param father Upper level of JSONObject.
     * @param name The name of the key.
     * @return The exist string value or "null".
     */
    public static String checkNameValidityThenReturnString(JSONObject father, String name, String defValue) {
        try {
            if (!father.isNull(name)) {
                // Value exist
                return father.getString(name);
            }
        } catch (JSONException e) {
            Log.e(TAG, "checkNameValidityThenReturnString: name = " + name + ", ", e);
        }
        return defValue;
    }

    public static String checkNameValidityThenReturnString(JSONObject father, String name) {
        return checkNameValidityThenReturnString(father, name, "null");
    }

    public static double checkNameValidityThenReturnDouble(JSONObject father, String name, double defValue) {
        try {
            if (!father.isNull(name)) {
                // Value exist
                return father.getDouble(name);
            }
        } catch (JSONException e) {
            Log.e(TAG, "checkNameValidityThenReturnDouble: ", e);
        }
        return 0.0;
    }

    public static double checkNameValidityThenReturnDouble(JSONObject father, String name) {
        return checkNameValidityThenReturnDouble(father, name, 0.0);
    }

}
