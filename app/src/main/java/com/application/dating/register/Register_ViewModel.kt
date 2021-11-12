package com.application.dating.register

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Register_ViewModel : ViewModel() {
    private var progress_double = MutableLiveData<Double>()
    val selectItem: LiveData<Double> get() = progress_double
    private var text_username = MutableLiveData<String>()
    val email : LiveData<String> get() = text_username
    private var text_password = MutableLiveData<String>()
    val password : LiveData<String> get() = text_password
    private var text_name = MutableLiveData<String>()
    val name : LiveData<String> get() = text_name
    private var text_dateofbirth = MutableLiveData<String>()
    val dateofbirth : LiveData<String> get() = text_dateofbirth
    private var text_gender = MutableLiveData<String>()
    val gender : LiveData<String> get() = text_gender
    private var text_gender_requirement = MutableLiveData<String>()
    val gender_requirement : LiveData<String> get() = text_gender_requirement
    private var url_id = MutableLiveData<Uri>()
    val get_url_id: LiveData<Uri> get() = url_id
    fun selectItem(item : Double){
        progress_double.value = item
    }
    fun username(email : String){
        text_username.value = email
    }
    fun password(password : String){
        text_password.value = password
    }
    fun name(name : String){
        text_name.value = name
    }
    fun dateofbirth(dateofbirth : String){
        text_dateofbirth.value = dateofbirth
    }
    fun gender(gender : String){
        text_gender.value = gender
    }
    fun gender_requirement(gender_requirement : String){
        text_gender_requirement.value = gender_requirement
    }
    fun url_id(url : Uri){
        url_id.value = url
    }
}