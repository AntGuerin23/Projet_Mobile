package com.example.projet_mobile.modals

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream

class PictureConverter {

    companion object PictureConverter {
        fun convertDrawableToByteArray(picture: Drawable): ByteArray {
            val bitmap = picture.toBitmap()
            val buffer = ByteArrayOutputStream(bitmap.width * bitmap.height)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer)
            return buffer.toByteArray()
        }
    }
}