package com.photoeditor.photoeffect.model

import com.google.gson.annotations.SerializedName

class FilterData {
    @SerializedName("text")
    var text: String
    @SerializedName("red")
    var red: Float
    @SerializedName("green")
    var green: Float
    @SerializedName("blue")
    var blue: Float
    @SerializedName("saturation")
    var saturation: Float

    constructor(text: String, red: Float, green: Float, blue: Float, saturation: Float) {
        this.text = text
        this.red = red
        this.green = green
        this.blue = blue
        this.saturation = saturation
    }

}