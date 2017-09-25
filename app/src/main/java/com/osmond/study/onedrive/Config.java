package com.osmond.study.onedrive;

import android.net.Uri;

/**
 * Config is a singleton class that contains the values used throughout the SDK.
 */
enum Config {
    INSTANCE;

    private Uri apiUri;
    private String apiVersion;
    private Uri oAuthAuthorizeUri;
    private Uri oAuthDesktopUri;
    private Uri oAuthLogoutUri;
    private Uri oAuthTokenUri;

    Config() {
        // appInitialize default values for constants
        apiUri = Uri.parse("https://apis.live.net/v5.0");
        apiVersion = "5.0";
        oAuthAuthorizeUri = Uri.parse("https://login.live.com/oauth20_authorize.srf");
        oAuthDesktopUri = Uri.parse("https://login.live.com/oauth20_desktop.srf");
        oAuthLogoutUri = Uri.parse("https://login.live.com/oauth20_logout.srf");
        oAuthTokenUri = Uri.parse("https://login.live.com/oauth20_token.srf");
    }

    public Uri getApiUri() {
        return apiUri;
    }


    public String getApiVersion() {
        return apiVersion;
    }


    public Uri getOAuthAuthorizeUri() {
        return oAuthAuthorizeUri;
    }


    public Uri getOAuthDesktopUri() {
        return oAuthDesktopUri;
    }


    public Uri getOAuthLogoutUri() {
        return oAuthLogoutUri;
    }


    public Uri getOAuthTokenUri() {
        return oAuthTokenUri;
    }

}