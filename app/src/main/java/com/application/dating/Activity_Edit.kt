package com.application.dating

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.application.dating.api.Dating_App_API
import com.application.dating.api.ServiceBuilder
import com.application.dating.model.Taikhoan
import com.application.dating.model.Taikhoan_hinhanh
import com.application.dating.register.UploadRequestBody
import com.application.dating.register.fragment.Register_Avatar_Fragment
import com.application.dating.register.getFileName
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.bottomsheet_choose_image.view.*
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.register_avatar_fragment.*
import kotlinx.android.synthetic.main.register_gender_fragment.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

class Activity_Edit : AppCompatActivity() ,UploadRequestBody.UploadCallback{
    private var gender : String ?= null
     lateinit var myApi : Dating_App_API
    private val STORAGE_PERMISSION_CODE = 113
    var compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        myApi = ServiceBuilder.getInstance().create(Dating_App_API::class.java)
        loadimage()
        getMyData()
        back.setOnClickListener {
            var dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_loading)
            dialog.show()
            updateData(dialog)
        }
        man_button.setOnClickListener {
            man_text.setTextColor(Color.RED)
            woman_text.setTextColor(Color.GRAY)
            it.background = ContextCompat.getDrawable(man_button.context,R.drawable.ic_check_select)
            woman_button.background = ContextCompat.getDrawable(man_button.context,R.drawable.ic_check_unselect)
            gender = "nam"
        }
        woman_button.setOnClickListener {
            woman_text.setTextColor(Color.RED)
            man_text.setTextColor(Color.GRAY)
            it.background = ContextCompat.getDrawable(man_button.context,R.drawable.ic_check_select)
            man_button.background = ContextCompat.getDrawable(man_button.context,R.drawable.ic_check_unselect)
            gender = "nữ"
        }
        val bottomSheetDialog = BottomSheetDialog(this@Activity_Edit)
        val view2 = layoutInflater.inflate(R.layout.bottomsheet_choose_image, null)
        bottomSheetDialog.setContentView(view2)
        btn_addimage.setOnClickListener {
            bottomSheetDialog.show()
            bottomSheetDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        view2.ll_choose_gallery.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
                // permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions,STORAGE_PERMISSION_CODE)
            }else{
                pickImageFromGallery()
            }
            bottomSheetDialog.dismiss()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,STORAGE_PERMISSION_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == STORAGE_PERMISSION_CODE) {
            val selectedImage = data?.data
            var dialog = Dialog(this@Activity_Edit)
            dialog.setContentView(R.layout.dialog_loading)
            dialog.show()
            uploadImage(selectedImage!!,dialog)
        }
    }

    private fun uploadImage(selectedImage : Uri, dialog: Dialog) {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImage, "r", null) ?: return
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImage))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file, "image", this)
        Dating_App_API().uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create(MediaType.parse("text/plain"), MainActivity.id.toString()),
            RequestBody.create(MediaType.parse("text/plain"), "false")
        ).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                dialog.dismiss()
                progress_barrrr.progress = 0
                Toast.makeText(this@Activity_Edit, t.message, Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(
                call: Call<String>,
                response: retrofit2.Response<String>
            ){
                    progress_barrrr.progress = 100
                    loadimage()
                    dialog.dismiss()
            }
        })
    }

    private fun getMyData(){
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("http://192.168.1.8:8086/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Dating_App_API::class.java)
            val retrofitData =  retrofitBuilder.getProfile(MainActivity.id!!)
            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val gson = Gson()
                    val acc = gson.fromJson(response.body(), Taikhoan::class.java)
                    if(acc != null){
                        edt_introduce.setText(acc.gioithieubanthan.toString())
                        edt_city.setText(acc.dangsongtai)
                        edt_countryside.setText(acc.quequan)
                        if(acc.gioitinh.equals("nam")){
                            man_text.setTextColor(Color.GRAY)
                            man_text.setTextColor(Color.RED)
                            man_button.background = ContextCompat.getDrawable(man_button.context,R.drawable.ic_check_select)
                            woman_button.background = ContextCompat.getDrawable(man_button.context,R.drawable.ic_check_unselect)
                        }else{
                            man_text.setTextColor(Color.GRAY)
                            woman_text.setTextColor(Color.RED)
                            man_button.background = ContextCompat.getDrawable(man_button.context,R.drawable.ic_check_unselect)
                            woman_button.background = ContextCompat.getDrawable(man_button.context,R.drawable.ic_check_select)
                        }
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@Activity_Edit,t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    private fun updateData(dialog: Dialog) {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("http://192.168.1.8:8086/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Dating_App_API::class.java)
        val acc = Taikhoan (
            dangsongtai = edt_city.text.toString(),
            gioithieubanthan = edt_introduce.text.toString(),
            quequan = edt_countryside.text.toString(),
            gioitinh = gender,
            sothich = edt_interests.text.toString()
        )
            val retrofitData = retrofitBuilder.updateprofile(MainActivity.id!!, acc)
            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    dialog.dismiss()
                    finish()
                    Toast.makeText(this@Activity_Edit, "thành công", Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    dialog.dismiss()
                    finish()
                    Toast.makeText(this@Activity_Edit, "thất bại", Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun loadimage(){
        val hinhanh = Taikhoan_hinhanh(
            id_taikhoan = MainActivity.id!!,
            is_anhdaidien = false
        )
        compositeDisposable.addAll(myApi.getimage(hinhanh)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({s ->
                displayData(s)
            },{t :Throwable? ->
                Toast.makeText(this@Activity_Edit,t!!.message,Toast.LENGTH_SHORT).show()
            }))
    }
    private fun displayData(list : List<Taikhoan_hinhanh>){
        val imageList = ArrayList<SlideModel>()
        val header_path = "http://192.168.1.8:8086"
        for(img in list){
            imageList.add(SlideModel(header_path+img.duongdan))
        }
        imageSlider.setImageList(imageList,ScaleTypes.FIT)
    }
    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onProgressUpdate(percentage: Int) {
    }
}