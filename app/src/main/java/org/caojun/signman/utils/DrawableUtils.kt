package org.caojun.signman.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream

/**
 * Created by CaoJun on 2017/9/5.
 */
object DrawableUtils {
    
    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        val bmp = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bmp
    }
    
    fun toByteArray(drawable: Drawable): ByteArray {
//        val bd = drawable as BitmapDrawable
//        val bitmap = bd.bitmap
        val bitmap = getBitmapFromDrawable(drawable)
        val os = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        return os.toByteArray()
    }

    fun toDrawable(data: ByteArray): Drawable {
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size, null)
        return BitmapDrawable(null, bitmap)
    }
}
