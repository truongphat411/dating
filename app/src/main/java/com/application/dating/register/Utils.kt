package com.application.dating.register

import android.content.Context
import android.graphics.Bitmap
import java.io.File

interface Utils {
    fun bitmaptofile(filename : String,context: Context, bm : Bitmap) : File

    fun reduceBitmapSize(bm : Bitmap, MAX_SIZE : Int) : Bitmap
}