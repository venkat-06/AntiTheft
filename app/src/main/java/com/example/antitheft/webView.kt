package com.example.antitheft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class webView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val webView: WebView = findViewById(R.id.web)
        //webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        val url = "https://www.ceir.gov.in/Home/index.jsp#"
        webView.loadUrl(url)
    }
}