package com.ferranpons.issposition

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView

class WebViewActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        val webView = findViewById(R.id.webView) as WebView
        webView.loadUrl("https://github.com/ferranponsscmspain/iss-position")
    }
}
