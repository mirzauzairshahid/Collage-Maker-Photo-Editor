package com.photoeditor.photoblur

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import com.facebook.ads.InterstitialAd
import com.google.android.gms.ads.AdRequest

import com.photoeditor.photoeffect.R

class AdLoader {

    internal var isAdsShown = true
    internal var countDownTimer: CountDownTimer? = null

    var mInterstitialAd: com.google.android.gms.ads.InterstitialAd? = null

    var interstitialAd: com.facebook.ads.InterstitialAd? = null

    var fbad: String? = null

    fun StartTimer() {
        try {
            if (countDownTimer != null) {
                countDownTimer!!.cancel()
                countDownTimer = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        isAdsShown = false
        countDownTimer = object : CountDownTimer(10000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                // TODO Auto-generated method stub
            }

            override fun onFinish() {
                // TODO Auto-generated method stub
                isAdsShown = true
            }
        }.start()
    }

    fun loadFullAdFacebook(context: Context) {
            try {
               // AdSettings.addTestDevice("18c9a323-0d23-4bb6-aff3-04d9a864c4ca");
                interstitialAd = InterstitialAd(context, context.getString(R.string.fb_adid))

             //   interstitialAd!!.loadAd()
                Log.e("Ad Requested","Load")
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }
    fun loadfullAdAdmob(context: Context){

        mInterstitialAd = com.google.android.gms.ads.InterstitialAd(context)
        mInterstitialAd!!.adUnitId = context.getString(R.string.Admob_adUnitId)
        mInterstitialAd!!.loadAd(AdRequest.Builder().build())

    }
    fun ShowFBAds(context: Context) {
        try {
            if (interstitialAd != null && interstitialAd!!.isAdLoaded) {
                interstitialAd!!.show()
                Log.e("Ad Shown","Show")
            }else{
                showInterstitial(context)
                Log.e("Google Show called","Called")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        loadFullAdFacebook(context)

    }
    companion object {
        var adLoader: AdLoader? = null
        val ads: AdLoader
            get() {
                if (adLoader == null) {
                    adLoader = AdLoader()
                }
                return adLoader!!
            }
    }

    fun showInterstitial(context: Context) {
        Log.e("FunCalled", "From ShowInterstitial")

        if (mInterstitialAd!!.isLoaded) {
            Log.e("Google in show", "From ShowInterstitial")
            mInterstitialAd!!.show()
        }
        loadfullAdAdmob(context)
    }
}
