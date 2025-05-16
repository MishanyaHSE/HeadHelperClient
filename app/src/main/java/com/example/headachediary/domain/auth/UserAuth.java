package com.example.headachediary.domain.auth;

import androidx.annotation.NonNull;

public class UserAuth {
    private String username;
    private String password;




    public String getUsername() {
        return this.username;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return "username=" + username + ";password=" + password;
    }
}
