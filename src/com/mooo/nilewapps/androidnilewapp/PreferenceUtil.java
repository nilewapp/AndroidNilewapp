package com.mooo.nilewapps.androidnilewapp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class PreferenceUtil {

    /**
     * Stores a preference with a key stored in a resource
     * @param activity
     * @param id resource id that corresponds to the preference key
     * @param value new value
     */
    public static void storePreference(Activity activity, int id, String value) {
        final String resKey = activity.getString(id);
        
        activity.getPreferences(Context.MODE_PRIVATE)
            .edit()
            .putString(resKey, value)
            .commit();
    }
    
    /**
     * Gets a preference string from a key stored in a resource
     * @param activity
     * @param id resource id that corresponds to the preference key
     * @param defValue return value if there is no preference with the given key
     * @return
     */
    public static String getPreference(Activity activity, int id, String defValue) {
        final String resValue = activity.getString(id);
        return activity.getPreferences(Context.MODE_PRIVATE).getString(resValue, defValue);
    }
    
    public static void storeBitmap(Context context, Bitmap bitmap, String filename)
            throws IOException {
        final FileOutputStream fs = context.openFileOutput(filename, Context.MODE_PRIVATE);
        bitmap.compress(CompressFormat.PNG, 90, fs);
    }
    
    public static Bitmap getBitmap(Context context, String filename)
            throws FileNotFoundException {
        InputStream is = context.openFileInput(filename);
        return BitmapFactory.decodeStream(is);
    }
}
