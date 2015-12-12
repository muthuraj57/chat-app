package com.muthuraj.chat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Muthuraj on 1/7/2015.
 */
public class About extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        //to change title
        setTitle("About");

        WebView webView = (WebView)findViewById(R.id.webView);

        //to handle external urls in browser
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                if (Uri.parse(url).getHost().length() == 0)
                {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                return true;
            }
        });

        //load local html file
        webView.loadUrl("file:///android_asset/about.html");
    }
}
