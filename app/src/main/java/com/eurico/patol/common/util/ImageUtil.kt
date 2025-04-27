package com.eurico.patol.common.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object ImageUtil {
    fun converterBase64ToImg(base64String: String): Bitmap? {
        val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return decodedImage
    }
}