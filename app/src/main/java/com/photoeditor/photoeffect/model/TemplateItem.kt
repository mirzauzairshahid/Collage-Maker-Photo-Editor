package com.photoeditor.photoeffect.model

import com.photoeditor.photoeffect.template.PhotoItem
import com.photoeditor.photoeffect.template.PhotoLayout

import java.util.ArrayList

/**
 * Created by vanhu_000 on 3/25/2016.
 */
class TemplateItem : ImageTemplate {
    var sectionManager: Int = 0
    var sectionFirstPosition: Int = 0
    var isHeader = false
    var header: String? = null

    var isAds = false
    var photoItemList: ArrayList<PhotoItem> = ArrayList()

    constructor() {

    }

    constructor(template: ImageTemplate) {
        languages = template.languages
        packageId = template.packageId
        preview = template.preview
        mtemplate = template.mtemplate
        child = template.child
        title = template.title
        thumbnail = template.thumbnail
        selectedThumbnail = template.selectedThumbnail
        isSelected = template.isSelected
        // To be used to display
        showingType = template.showingType
        // To be used in database
        lastModified = template.lastModified
        status = template.status
        id = template.id
        photoItemList = PhotoLayout.parseImageTemplate(template)
    }
}
