package com.osmond.study;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Base64OutputStream;
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

//public class GoogleDriveTest extends AppCompatActivity {
public class GoogleDriveTest extends Activity {

//    private static final String sClientID = "1014172469438-dv90limts9g6o1ert79mbhdq1n506q63.apps.googleusercontent.com";
    // for iphone
    // - problems:  redirect_url mismatch
    private static final String sClientID = "1021466473229-gfuljuu4spgkvs4vnk6hl48ah1rcpfre.apps.googleusercontent.com";

    // for Web
//    private static final String sClientID = "1021466473229-t5pevqkp3ecbmsdpu5tfvqk4lsc2cmah.apps.googleusercontent.com";



    /// "com.osmond.study://"
    private static final String sRedirect_URL= "com.googleusercontent.apps.1021466473229-gfuljuu4spgkvs4vnk6hl48ah1rcpfre";

    String mAuthorizationCode = null;

    String mAccessToken = null;
    String mTokenType = null;
    long mExpired_In = 0;
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
        mInfo.setMovementMethod(ScrollingMovementMethod.getInstance()); // attach scrolling
        mInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mInfo.setText("");
                return true;
            }
        });

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

        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refersh();
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp=getSharedPreferences("GoogleDriveTest",MODE_APPEND);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("mAuthorizationCode",mAuthorizationCode);
                edit.putString("mAccessToken",mAccessToken);
                edit.putString("mRefreshToken",mRefreshToken);
                edit.putString("mTokenType",mTokenType);
                edit.putLong("mExpired_In",mExpired_In);
                edit.apply();
                addLine("save tokens succeed");

            }
        });

        findViewById(R.id.load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp=getSharedPreferences("GoogleDriveTest",MODE_APPEND);
                mAuthorizationCode = sp.getString("mAuthorizationCode",null);
                mAccessToken=sp.getString("mAccessToken",null);
                mRefreshToken=sp.getString("mRefreshToken",null);
                mTokenType = sp.getString("mTokenType",null);
                mExpired_In = sp.getLong("mExpired_In",0);
                addLine("load token succeed");
                dumpTokens();
            }
        });

        findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAbout();
            }
        });

        findViewById(R.id.list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onList();
            }
        });
    }

    private void dumpTokens(){
        addLine("dump tokens:");

        addLine("===authorization code===");
        addLine(mAuthorizationCode);
        Log.v("dump-mAuthorizationCode","\n"+mAuthorizationCode);

        addLine("===access_token===");
        addLine((mAccessToken));
        Log.v("dump-mAccessToken","\n"+mAccessToken);

        addLine("===token_type===");
        addLine(mTokenType);
        Log.v("dump-mTokenType","\n"+mTokenType);

        addLine("===expires_in===");
        addLine(mExpired_In + "");
        Log.v("dump-mExpired_In","\n"+mExpired_In+"");

        addLine("===refresh_token===");
        addLine(mRefreshToken);
        Log.v("dump-mRefreshToken","\n"+mRefreshToken);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            Log.v("onResume,intent", intent.toString());
            parseOAuthIntent(intent);

        }
    }

    private void addLine(final String str) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInfo.append(str + "\n");
                scrollToLastLine();
            }
            private void scrollToLastLine(){
                // auto scroll to last lint
                int lineCount= mInfo.getLineCount();
                int lintHeight =mInfo.getLineHeight();
                int height =mInfo.getHeight();
                int offset = lineCount*lintHeight;
                if(offset > height){
                    mInfo.scrollTo(0,offset-height);
                }
            }
        });
    }


    private void parseOAuthIntent(Intent intent) {
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
        if (!sRedirect_URL.equals(scheme)) {
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
        addLine("===authorization code===");
        addLine(code);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseOAuthIntent(intent);

    }

    private void onConnectOAuth2() {
        try {
            String host = "accounts.google.com";
            String path = "/o/oauth2/v2/auth";

            Locale locale = Locale.getDefault();

            String[] params = {
                    "scope", "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/drive ",
                    "response_type", "code",
                    "redirect_uri", sRedirect_URL+"://",
                    // for web
//                    "redirect_uri", "https://www.skydrm.com/rms/OAuthManager/GDAuth/gdAuthFinish",
                    "client_id", sClientID,
                    "state", "oauth2:156da5e9576ad692a101a15e2ce53d57"};

            String url = buildUrlWithParams(locale.toString(), host, path, params);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onExchange() {
        // to get tokens
        class Handler {
            Runnable get() {
                return new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();

                        RequestBody body = new FormBody.Builder()
                                .add("grant_type", "authorization_code")
                                .add("client_id", sClientID)
                                .add("code", mAuthorizationCode)
                                .add("redirect_uri", sRedirect_URL+"://")
                                .build();


                        Request request = new Request.Builder()
                                .post(body)
                                .url("https://www.googleapis.com/oauth2/v4/token")
                                .build();

                        Response response = null;
                        String result = null;
                        try {
                            response = client.newCall(request).execute();
                            result = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            addLine("exchange failed:" + e.toString());
                            return;
                        }
                        if (!response.isSuccessful()) {
                            addLine("exchange failed:" + result);
                            return;
                        }

//                        addLine("exchange result:"+result);
                        Log.v("exchange result:", result);

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
                            JSONObject jresult = new JSONObject(result);
                            addLine("===access_token===");
                            mAccessToken = jresult.getString("access_token");
                            addLine((mAccessToken));

                            addLine("===token_type===");
                            mTokenType = jresult.getString("token_type");
                            addLine(mTokenType);

                            addLine("===expires_in===");
                            mExpired_In = jresult.getLong("expires_in");
                            addLine(mExpired_In + "");

                            addLine("===refresh_token===");
                            mRefreshToken = jresult.getString("refresh_token");
                            addLine(mRefreshToken);

                            addLine("===id_token===");
                            String id_token = jresult.getString("id_token");
                            addLine(id_token);

                            // try to extract user id fro id_token , see https://jwt.io/ and JSON Web Token (JWT)
                            if(id_token == null){
                                return;
                            }
                            String validValue= id_token.substring(id_token.indexOf('.')+1,id_token.lastIndexOf('.'));
                            // base 64 decode
                            String jwt= new String(Base64.decode(validValue,Base64.DEFAULT));
                            //
                            addLine("===debug_jwt==");
                            addLine(jwt);

                            // extract "sub" as userID from the json jwt
                            addLine("===userID===");
                            String userID = new JSONObject(jwt).getString("sub");
                            addLine(userID);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
            }
        }

        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Handler().get());
    }


    private void refersh() {
        // post to  https://www.googleapis.com/oauth2/v4/token
        class Handler {
            Runnable get() {
                return new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        FormBody body = new FormBody.Builder()
                                .add("refresh_token", mRefreshToken)
                                .add("client_id", sClientID)
                                //iphone
//                                .add("refresh_token","1/O3ng4tqztsKP7-nPw4n4DI45knCB6mqxF3eBFRyPfNPD_BXmJqnDJk8s7G6qxh_z")
//                                .add("client_id","1021466473229-gfuljuu4spgkvs4vnk6hl48ah1rcpfre.apps.googleusercontent.com")
                                .add("grant_type", "refresh_token")
                                .build();
                        Request request = new Request.Builder()
                                .url("https://www.googleapis.com/oauth2/v4/token")
                                .post(body)
                                .build();

                        Response response;
                            String result;
                        try {
                            response = client.newCall(request).execute();
                            result = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        if (!response.isSuccessful()) {
                            return;
                        }
                        Log.v("refresh result:", result);
                        // parse result
                        try {
                            JSONObject jresult = new JSONObject(result);

                            addLine("refresh result:");

                            addLine("===access_token===");
                            mAccessToken = jresult.getString("access_token");
                            addLine(mAccessToken);

                            addLine("===expires_in===");
                            mExpired_In =jresult.getLong("expires_in");
                            addLine(mExpired_In+"");

                            addLine("===token_type===");
                            mTokenType =jresult.getString("token_type");
                            addLine(mTokenType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                };
            }

            ;
        }

        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Handler().get());
    }

    // API about
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
                                .addHeader("Authorization", mTokenType + " " + mAccessToken)
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
                            result = response.body().string();
                            Log.v("OnAbout result", result);
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

        if (mAuthorizationCode == null) {
            addLine("mAuthorizationCode==null");
            return;
        }

        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Handler().get());

    }

    private void onList(){
        class Handler{
            Runnable get(){
                return new Runnable() {
                    @Override
                    public void run() {
                        // GET https://www.googleapis.com/drive/v3/about
                        String fields_param="fields=incompleteSearch,nextPageToken,files(id,name,mimeType,modifiedTime,size)";
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("https://www.googleapis.com/drive/v3/files?q='root' in parents and trashed != true"+"&"+fields_param)
                                .get()
                                .addHeader("Authorization", mTokenType + " " + mAccessToken)
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
                            result = response.body().string();
                            Log.v("onList result", result);
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
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Handler().get());
    }

    private void onCreate(){
        class Handler{
            Runnable get(){
                return new Runnable() {
                    @Override
                    public void run() {

                    }
                };
            }
        }
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Handler().get());
    }

    private void onDelete(){
        class Handler{
            Runnable get(){
                return new Runnable() {
                    @Override
                    public void run() {

                    }
                };
            }
        }
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Handler().get());
    }

    private void onUpload(){
        class Handler{
            Runnable get(){
                return new Runnable() {
                    @Override
                    public void run() {

                    }
                };
            }
        }
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Handler().get());
    }

    private void onDownload(){
        class Handler{
            Runnable get(){
                return new Runnable() {
                    @Override
                    public void run() {

                    }
                };
            }
        }
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Handler().get());
    }



}

