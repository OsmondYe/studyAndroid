package com.osmond.study;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView webView= (WebView) findViewById(R.id.webview);

        webView.loadUrl("https://accounts.google.com/o/oauth2/v2/auth?locale=en_US&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fdrive+&response_type=code&redirect_uri=com.googleusercontent.apps.1021466473229-gfuljuu4spgkvs4vnk6hl48ah1rcpfre%3A%2F%2F&client_id=1021466473229-gfuljuu4spgkvs4vnk6hl48ah1rcpfre.apps.googleusercontent.com&state=oauth2%3A156da5e9576ad692a101a15e2ce53d57");
    }
}
