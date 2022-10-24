package com.photoeditor.photoeffect.model

import com.google.gson.annotations.SerializedName

class GalleryAlbum {

    @SerializedName("mAlbumId")
    var mAlbumId: Long

    @SerializedName("mAlbumName")
    var mAlbumName: String

    @SerializedName("mImageList")
    var mImageList: ArrayList<String> = ArrayList()

    @SerializedName("mTakenDate")
    lateinit var mTakenDate: String

    constructor(mAlbumId: Long, mAlbumName: String) {
        this.mAlbumId = mAlbumId
        this.mAlbumName = mAlbumName
    }

}