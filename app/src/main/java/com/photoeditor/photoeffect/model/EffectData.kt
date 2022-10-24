package com.photoeditor.photoeffect.model

import com.google.gson.annotations.SerializedName

class EffectData {
    @SerializedName("name")
    var name: String

    @SerializedName("icon")
    var icon: Int

    constructor(name: String, icon: Int) {
        this.name = name
        this.icon = icon
    }

}