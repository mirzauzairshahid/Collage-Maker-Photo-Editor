package com.photoeditor.photoeffect.extensions

import android.content.Context
import android.content.pm.PackageManager

fun Context.isPackageInstalled(packageNameString: String): Boolean {
    return try {
        this.packageManager.getPackageInfo(packageNameString, PackageManager.GET_META_DATA)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}