package com.osmond.study;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Locale;

import okhttp3.OkHttpClient;

public class DropBoxTest extends AppCompatActivity {
    //RMS release
    public static final String NEXTLABS_GRANTED_KEY = "7os12uldmn5nlv9";
    public static final String NEXTLABS_GRANTED_SECRET = "yrghi8q31yqrb3n";

    static private final String NEXTLABS_CLIENT_ID = "SkyDRM-Android/Nextlabs";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_box_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.OAuth2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOauth2();
            }
        });
    }

    @Override
    protected void onResume() {
        Log.v("dropbox", "onResume");
        super.onResume();
        Intent intent=getIntent();
        if(intent!=null){
            Log.v("onResume,intent",intent.toString());
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.v("onNewIntent",intent.toString());
    }

    private void onOauth2() {
        try {
            String host ="www.dropbox.com";
            String path = "/oauth2/authorize";
            Locale locale = Locale.getDefault();

            String[] params = {
                    "response_type", "token",
                    "redirect_uri", "db-7os12uldmn5nlv9://callback",
                    "client_id", "7os12uldmn5nlv9",
                    "state", "oauth2:156da5e9576ad692a101a15e2ce53d57"};

            String url = buildUrlWithParams(locale.toString(), host, path, params);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
