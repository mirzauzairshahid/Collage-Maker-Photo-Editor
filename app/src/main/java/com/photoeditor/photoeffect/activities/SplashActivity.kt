package com.photoeditor.photoeffect.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.photoeditor.photoeffect.R

class SplashActivity : AppCompatActivity() {

    companion object {
        var isFromSplash: Boolean = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        nextActivity()
    }
    fun nextActivity() {
        isFromSplash = true
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
