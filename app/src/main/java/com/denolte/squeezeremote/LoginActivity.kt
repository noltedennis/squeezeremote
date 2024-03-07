package com.denolte.squeezeremote

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doOnTextChanged


class LoginActivity : Activity() {
    private lateinit var pref: SharedPreferences
    private var fromOrientation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        pref = applicationContext.getSharedPreferences(prefName, 0)

        // get ui elements
        val webpageUrl: EditText = findViewById(R.id.webpage_url)
        val username: EditText = findViewById(R.id.username)
        val password: EditText = findViewById(R.id.password)
        val goBtn: Button = findViewById(R.id.go)

        // enable button if webpageUrl contains valid URL
        webpageUrl.doOnTextChanged{text, _, _, _ ->
            goBtn.isEnabled = canSwitchActivity(text)
        }

        // save settings and goto MainActivity on button press
        goBtn.setOnClickListener{
            switchMainActivity(
                webpageUrl.text.toString(),
                username.text.toString(),
                password.text.toString()
            )
        }

        // initialize UI from shared preferences
        webpageUrl.setText(pref.getString(prefWebpageUrl, ""))
        username.setText(pref.getString(prefUserName, ""))
        password.setText(pref.getString(prefPassword, ""))

        // if url is set, directly switch to main activity if not caused by orientation reload
        if (canSwitchActivity(webpageUrl.text.toString())
            and !pref.getBoolean(prefFromOrientation, false)
        ) {
            switchMainActivity(
                webpageUrl.text.toString(),
                username.text.toString(),
                password.text.toString()
            )
        }
    }

    override fun onRetainNonConfigurationInstance(): Any? {
        if (!fromOrientation) {
            val editor = pref.edit()
            editor.putBoolean(prefFromOrientation, true)
            editor.apply()
        }
        return super.onRetainNonConfigurationInstance()
    }

    override fun onDestroy() {
        if (fromOrientation) {
            val editor = pref.edit()
            editor.putBoolean(prefFromOrientation, false)
            editor.apply()
        }
        super.onDestroy()
    }

    private fun canSwitchActivity(webpageUrl: CharSequence?): Boolean {
        return !(webpageUrl.isNullOrBlank() or !URLUtil.isValidUrl(webpageUrl.toString()))
    }

    // save prefs before switching and hand them along
    private fun switchMainActivity(webpageUrl: String, username: String, password: String) {
        val editor = pref.edit()
        editor.putString(prefWebpageUrl, webpageUrl)
        editor.putString(prefUserName, username)
        editor.putString(prefPassword, password)
        editor.putBoolean(prefFromOrientation, false)
        editor.apply()

        val intent = Intent(applicationContext,  MainActivity::class.java)
        intent.putExtra(prefWebpageUrl, webpageUrl)
        intent.putExtra(prefUserName, username)
        intent.putExtra(prefPassword, password)
        startActivity(intent)
    }

    companion object {
        const val prefName = "squeeze_viewer"
        const val prefWebpageUrl = "webpage_url"
        const val prefUserName = "user"
        const val prefPassword = "password"
        const val prefFromOrientation = "from_orientation"
    }
}