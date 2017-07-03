package com.osmond.study;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GoogleDriveTest extends AppCompatActivity {


    String mAuthorizationCode = null;

    String mAccessToken=null;
    String mTokenType = null;
    long expired_in =0;
    String mRefreshToken = null;

    TextView mInfo = null;

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
                                            /*@Nullable*/String/*@Nullable*/[] params) throws UnsupportedEncodingException, URISyntaxException {
        return buildUri(host, path) + "?" + encodeUrlParams(userLocale, params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_drive_test);

        mInfo = (TextView) findViewById(R.id.info);

        findViewById(R.id.link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConnectOAuth2();
            }
        });

        findViewById(R.id.exchange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExchange();
            }
        });

        findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAbout();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            Log.v("onResume,intent", intent.toString());
        }
    }

    private void addLine(final String str) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInfo.append(str + "\n");
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null) {
            return;
        }
        Log.v("onNewIntent", intent.toString());
        Uri uri = intent.getData();
        if (uri == null) {
            return;
        }
        Log.v("onNewIntent", uri.toString());
        String scheme = uri.getScheme();
        if (scheme == null) {
            return;
        }
        if (!"com.osmond.study".equals(scheme)) {
            return;
        }
        String part = uri.getSchemeSpecificPart();
        if (part == null) {
            return;
        }
        // exctract access token
        // i.e.   //?state=oauth2:156da5e9576ad692a101a15e2ce53d57&code=4/a1pGXv5C0wCxHOXE0bSwkUvW-4Hd_Ujvlc9JnBZzySw
        String code = part.substring(part.indexOf("code=") + "code=".length());
        if (code == null) {
            return;
        }
        if (code.isEmpty()) {
            return;
        }
        Log.v("onNewIntent", "retrived the token:" + code);

        mAuthorizationCode = code;

        addLine("AuthorizationCode:");
        addLine(code);

    }

    private void onConnectOAuth2() {
        try {
            String host = "accounts.google.com";
            String path = "/o/oauth2/v2/auth";

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


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onExchange(){
        // to get tokens
        class Handler{
            Runnable get(){
                return  new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();

                        RequestBody body = new FormBody.Builder()
                                .add("grant_type", "authorization_code")
                                .add("client_id", "1014172469438-dv90limts9g6o1ert79mbhdq1n506q63.apps.googleusercontent.com")
                                .add("code", mAuthorizationCode)
                                .add("redirect_uri","com.osmond.study://")
                                .build();


                        Request request = new Request.Builder()
                                .post(body)
                                .url("https://www.googleapis.com/oauth2/v4/token")
                                .build();

                        Response response=null;
                        String result=null;
                        try {
                            response = client.newCall(request).execute();
                            result =response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            addLine("exchange failed:"+e.toString());
                            return;
                        }
                        if (!response.isSuccessful()){
                            addLine("exchange failed:"+response.toString());
                            return;
                        }

//                        addLine("exchange result:"+result);
                        Log.v("exchange result:",result);

                        // begin to parse the token
                        /*
                         {
"access_token": "ya29.Glt8BIweSbMQ0qJGDJUi1cBpa_2c_K9yroYrEYxKVGZryioqEvDhH_nPcBjxAR8wCXnDeq4NvwUy5-O4Z-RT6yJ8M11YEq5J4bIm8L2qK4-fTW1effd7KKmUonaV",
"token_type": "Bearer",
"expires_in": 3600,
"refresh_token": "1/j3w3BRY2d3g1r-F7GSJSuKKLlzullYgCpIXbTCkaUZI"
}
                         */
                        try {
                            JSONObject jresult =new JSONObject(result);
                            addLine("Access_Token:");
                            mAccessToken=jresult.getString("access_token");
                            addLine((mAccessToken));

                            addLine("Token_Type:");
                            mTokenType = jresult.getString("token_type");
                            addLine(mTokenType);

                            addLine("Expired_In");
                            expired_in = jresult.getLong("expires_in");
                            addLine(expired_in +"");

                            addLine("Refresh_Token:");
                            mRefreshToken = jresult.getString("refresh_token");
                            addLine(mRefreshToken);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                };
            }
        }

        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Handler().get());
    }

    private void onAbout() {
        class Handler {
            Runnable get() {
                return new Runnable() {
                    @Override
                    public void run() {
                        // GET https://www.googleapis.com/drive/v3/about
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("https://www.googleapis.com/drive/v3/about?fields=kind,user,storageQuota")
                                .get()
                                .addHeader("Authorization",mTokenType+ " " + mAccessToken)
                                .build();

                        Response response = null;
                        try {
                            response = client.newCall(request).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        String result;
                        try {
                            result=response.body().string();
                            Log.v("OnAbout result",result);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        if (!response.isSuccessful()) {
                            return;
                        }
                        addLine(result);
                    }
                };

            }
        }

        if (mAuthorizationCode ==null){
            addLine("mAuthorizationCode==null");
            return;
        }

            AsyncTask.THREAD_POOL_EXECUTOR.execute(new Handler().get());

    }
}

