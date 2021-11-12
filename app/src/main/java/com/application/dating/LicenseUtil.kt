package com.application.dating

import androidx.fragment.app.FragmentActivity
import com.application.dating.R

/**
 * Created by Sergey Yakimchik on 10.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */
internal object LicenseUtil {
    /**
     * Reading the license from raw resource file
     * @param activity current activity
     * @return license byte array
     */
    fun getLicense(fragment: FragmentActivity): ByteArray? {
        try {
            val licInput = fragment.resources.openRawResource(R.raw.regula)
            val available = licInput.available()
            val license = ByteArray(available)
            licInput.read(license)
            licInput.close()
            return license
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }
}