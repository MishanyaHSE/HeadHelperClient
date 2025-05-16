package com.example.headachediary.data.source.util.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String APP_PREFERENCES = "myprefs";
    private static final String TOKEN_KEY = "token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";

    private SharedPreferences preferences;

    public TokenManager(Context context) {
        preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void saveToken(String token, String refreshToken) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.putString(REFRESH_TOKEN_KEY, refreshToken);
        editor.apply();
    }

    public String getToken() {
        return preferences.getString(TOKEN_KEY, null);
    }

    public String getRefreshToken() {
        return preferences.getString(REFRESH_TOKEN_KEY, null);
    }

    public void deleteTokens() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
