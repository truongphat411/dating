package com.application.dating.register

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Register_ViewModel : ViewModel() {
    private var progress_double = MutableLiveData<Double>()
    val selectItem: LiveData<Double> get() = progress_double
    fun selectItem(item : Double){
        progress_double.value = item
    }

}