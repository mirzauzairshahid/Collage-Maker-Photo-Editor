package com.photoeditor.photoeffect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds

class SplashActivity : AppCompatActivity() {

    companion object {
        var isFromSplash: Boolean = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //facebook Ads initialization
        AudienceNetworkAds.isInAdsProcess(this)
        AudienceNetworkAds.initialize(this)

        //google Ads initialization
        MobileAds.initialize(this){}
        nextActivity()
    }
    fun nextActivity() {
        isFromSplash = true
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
