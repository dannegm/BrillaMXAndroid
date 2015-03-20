package mx.ambmultimedia.brillamexico.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {
    private final String SHARED_PREFS_FILE = "BMXPrebfs";

    private Context mContext;

    public Config (Context context){
        mContext = context;
    }
    private SharedPreferences getSettings(){
        return mContext.getSharedPreferences(SHARED_PREFS_FILE, 0);
    }

    public String get (String key, String value) {
        return getSettings().getString(key, value);
    }
    public void set (String key, String value) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void clear () {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.clear();
        editor.commit();
    }
}
