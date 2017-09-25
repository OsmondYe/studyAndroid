package com.osmond.study.onedrive;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.osmond.study.DevLog;
import com.osmond.study.R;

import java.util.Comparator;
import java.util.Locale;

/**
 * Created by oye on 8/1/2017.
 */

public class OAuth2Activity extends Activity{
    String clientId="8ea12ff6-4f3f-4f4d-a727-d02e2be5e15e";
    String redirectUri = "https://login.live.com/oauth20_desktop.srf";
    String scope = "wl.signin wl.basic wl.emails wl.offline_access wl.skydrive_update wl.skydrive wl.contacts_create";

    DevLog log= new DevLog(OAuth2Activity.class.getSimpleName());

    Uri oAuthDesktopUri = Uri.parse("https://login.live.com/oauth20_desktop.srf");

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onedrive_oauth2);
        webView = (WebView) findViewById(R.id.webview);
        // config webView
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);


        webView.setWebViewClient(getWebViewClient());
        // config OneDrive OAuth2
        Uri oauth = Uri.parse("https://login.live.com/oauth20_authorize.srf")
                .buildUpon()
                .appendQueryParameter("client_id",clientId)
                .appendQueryParameter("scope",scope)
                .appendQueryParameter("display","android_phone")
                .appendQueryParameter("response_type","code")
                .appendQueryParameter("locale", Locale.getDefault().toString())
                .appendQueryParameter("redirect_uri",redirectUri)
                .build();

        webView.loadUrl(oauth.toString());
    }

    private WebViewClient getWebViewClient()  {
        return new WebViewClient(){
            @Override
            public void onLoadResource(WebView view, String url) {
                log.i("onLoadResource " + url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                log.i("shouldOverrideUrlLoading " + url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                log.i("shouldInterceptRequest " + url);
                return super.shouldInterceptRequest(view,url);
            }
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                log.i("onPageStarted " + url);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                log.i("onPageFinished " + url);
                Uri uri = Uri.parse(url);
                // this is the only entrance-place to judge whether
                // we got ok or not
                // as for document, https://login.live.com/oauth20_desktop.srf
                Uri endUri = oAuthDesktopUri;
                boolean isEndUri = UriComparator.INSTANCE.compare(uri, endUri) == 0;
                if (!isEndUri) {
                    return;
                }
                OAuth2Activity.this.onEndUri(uri);
            }
        };
    }

    private void onEndUri(Uri endUri){
        log.i("onEndUri" +endUri.toString());
        // on auth ok:  https://login.live.com/oauth20_desktop.srf?code=M2aa0ed32-cc13-443e-3a61-3e9c96a2b869&lc=1033
        // on auth failed: https://login.live.com/oauth20_desktop.srf?error=access_denied&error_description=The%20user%20has%20denied%20access%20to%20the%20scope%20requested%20by%20the%20client%20application.&lc=1033
        if(endUri.getQuery() != null){
            String code = endUri.getQueryParameter("code");
            if(code !=null){
                log.i("onEndUri "+"code="+code);
                return;
            }
            String error = endUri.getQueryParameter("error");
            if(error!=null){
                log.i("onEndUri "+"error="+error);
                String errorDescription = endUri.getQueryParameter("error_description");
                if(errorDescription!=null){
                    log.i("onEndUri "+"errorDescription="+errorDescription);
                }
                return;
            }
        }
    }

    private enum UriComparator implements Comparator<Uri> {
        INSTANCE;

        @Override
        public int compare(Uri lhs, Uri rhs) {
            String[] lhsParts = {lhs.getScheme(), lhs.getAuthority(), lhs.getPath()};
            String[] rhsParts = {rhs.getScheme(), rhs.getAuthority(), rhs.getPath()};

            for (int i = 0; i < lhsParts.length; i++) {
                int compare = lhsParts[i].compareTo(rhsParts[i]);
                if (compare != 0) {
                    return compare;
                }
            }

            return 0;
        }
    }

}
