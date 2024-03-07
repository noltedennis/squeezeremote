package com.denolte.squeezeremote.util

import android.webkit.*

class HttpAuthWebViewClient(private val userName: String,
                            private val password: String
    ) : WebViewClient() {
    override fun onReceivedHttpAuthRequest(
        view: WebView?,
        handler: HttpAuthHandler?,
        host: String?,
        realm: String?
    ) {
        handler?.proceed(userName, password)
        super.onReceivedHttpAuthRequest(view, handler, host, realm)
    }
}