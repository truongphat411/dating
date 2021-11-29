package com.application.dating.main.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.application.dating.Activity_Edit
import com.application.dating.MainActivity
import com.application.dating.R
import com.application.dating.api.Dating_App_API
import com.application.dating.api.ServiceBuilder
import com.application.dating.main.Check_ID
import com.application.dating.model.Taikhoan_hinhanh
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_profile_fragment.*
import kotlinx.android.synthetic.main.main_profile_fragment.view.*

class Main_PersonalPage_Fragment : Fragment (){
    lateinit var iMyAPI : Dating_App_API
    var compositeDisposable = CompositeDisposable()
    companion object{
         var bitmap : Bitmap ?= null
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_profile_fragment,container,false)
        iMyAPI = ServiceBuilder.getInstance().create(Dating_App_API::class.java)
        fetchdata(view)
        view.edit.setOnClickListener {
            val intent = Intent(requireContext(),Activity_Edit::class.java)
            startActivity(intent)
        }
        view.txt_comfirm.setOnClickListener {
            if(view.txt_comfirm.text.equals("Xác minh")){
                val intent = Intent(requireContext(),Check_ID::class.java)
                startActivity(intent)
            }
        }
        view.txtnameandage.text = MainActivity.ten.toString()
        return view
    }
    private fun fetchdata(view : View){
        val acc = Taikhoan_hinhanh(
            id_taikhoan = MainActivity.id!!,
            is_anhdaidien = true
        )
        compositeDisposable.addAll(iMyAPI.get_avatar(acc)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({s ->
                if(!s.contains("empty")){
                    val gson = Gson()
                    val img = gson.fromJson(s,Taikhoan_hinhanh::class.java)
                    val header_path = "http://192.168.1.8:8086"
                    val path = header_path+img.duongdan
                    Glide
                        .with(this)
                        .asBitmap()
                        .load(path)
                        .centerCrop()
                        .placeholder(R.drawable.monkey)
                        .into(object : CustomTarget<Bitmap>(){
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                bitmap = resource
                                view.profile_image.setImageBitmap(resource)
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                }
            },{t :Throwable? ->
                Toast.makeText(requireActivity(),t!!.message, Toast.LENGTH_SHORT).show()
            }))
    }

    override fun onResume() {
        super.onResume()
        if(MainActivity.is_xacminh!!){
            txt_comfirm.text = "Đã xác minh"
        }else{
            txt_comfirm.text = "Xác minh"
        }
    }
}