package com.example.headachediary.domain.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponse{

    @SerializedName("token_type")
    @Expose
    private String tokenType;

    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;

    public String getAccessToken() {
        return this.accessToken; }

    public String getRefreshToken() {
        return this.refreshToken; }
    public void setAccess_token(String access_token) {
        this.accessToken = access_token; }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken; }

    public String getToken_type() {
        return this.tokenType; }
    public void setToken_type(String token_type) {
        this.tokenType = token_type; }

}
