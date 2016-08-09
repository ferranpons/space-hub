package com.ferranpons.issposition;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_webview);
    WebView webView = (WebView) findViewById(R.id.webView);
    webView.loadUrl("https://github.com/ferranponsscmspain/iss-position");
  }
}
