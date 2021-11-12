package com.application.dating.register.repository

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File


/*fun ContentResolver.getFileName(fileUri: Uri) : String {
    var name = ""
    val cursor = query(fileUri, null, null, null)
   cursor?.use {
       it.moveToFirst()
       name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
   }
    return name
}*/

fun getFilename(contentResolver: ContentResolver, uri: Uri): String? {
    return when(uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.getString(nameIndex);
            }
        }
        ContentResolver.SCHEME_FILE-> {
            uri.path?.let { path ->
                File(path).name
            }
        }
        else -> null
    }
}