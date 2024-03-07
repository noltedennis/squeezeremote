package com.denolte.squeezeremote

import android.app.Activity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import com.denolte.squeezeremote.util.HttpAuthWebViewClient


class MainActivity : Activity() {
    private lateinit var webView: WebView
    private lateinit var webpageUrl: String
    private lateinit var userName: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // extract intent
        val intent = intent
        webpageUrl = intent.getStringExtra(LoginActivity.prefWebpageUrl) ?: ""
        userName = intent.getStringExtra(LoginActivity.prefUserName) ?: ""
        password = intent.getStringExtra(LoginActivity.prefPassword) ?: ""

        // setup web view
        webView = findViewById(R.id.webview)
        webView.webViewClient = HttpAuthWebViewClient(userName, password)

        // configure web view
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true
        webSettings.domStorageEnabled = true
    }

    override fun onResume() {
        super.onResume()

        // load content
        webView.loadUrl(webpageUrl)
    }
}