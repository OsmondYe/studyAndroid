package com.osmond.study;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Locale;

public class GoogleDriveTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_drive_test);

        findViewById(R.id.link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConnectOAuth2();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();
        if(intent!=null){
            Log.v("onResume,intent",intent.toString());
        }
    }

    public static String buildUri(String host, String path) throws URISyntaxException {
        return new URI("https", host, path, null).toASCIIString();
    }

    public static String encodeUrlParam(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8");
    }

    private static String encodeUrlParams(/*@Nullable*/String userLocale,
                                          /*@Nullable*/String/*@Nullable*/[] params) throws UnsupportedEncodingException {
        StringBuilder buf = new StringBuilder();
        String sep = "";
        if (userLocale != null) {
            buf.append("locale=").append(userLocale);
            sep = "&";
        }

        if (params != null) {
            if (params.length % 2 != 0) {
                throw new IllegalArgumentException("'params.length' is " + params.length + "; expecting a multiple of two");
            }
            for (int i = 0; i < params.length; ) {
                String key = params[i];
                String value = params[i + 1];
                if (key == null) throw new IllegalArgumentException("params[" + i + "] is null");
                if (value != null) {
                    buf.append(sep);
                    sep = "&";
                    buf.append(encodeUrlParam(key));
                    buf.append("=");
                    buf.append(encodeUrlParam(value));
                }
                i += 2;
            }
        }

        return buf.toString();
    }

    public static String buildUrlWithParams(/*@Nullable*/String userLocale,
                                            String host,
                                            String path,
                                            /*@Nullable*/String/*@Nullable*/[] params) throws UnsupportedEncodingException,URISyntaxException {
        return buildUri(host, path) + "?" + encodeUrlParams(userLocale, params);
    }

    private void onConnectOAuth2(){
        try{
            String host="accounts.google.com";
            String path="/o/oauth2/v2/auth";

            Locale locale = Locale.getDefault();

            String[] params = {
                    "scope", "https://www.googleapis.com/auth/drive",
                    "response_type", "code",
                    "redirect_uri", "com.osmond.study://",
                    "client_id", "1014172469438-dv90limts9g6o1ert79mbhdq1n506q63.apps.googleusercontent.com",
                    "state", "oauth2:156da5e9576ad692a101a15e2ce53d57"};

            String url = buildUrlWithParams(locale.toString(), host, path, params);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

