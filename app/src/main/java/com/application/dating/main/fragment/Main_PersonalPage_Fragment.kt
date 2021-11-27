package com.application.dating.main.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.application.dating.Activity_Edit
import com.application.dating.Activity_Setting
import com.application.dating.MainActivity
import com.application.dating.R
import com.application.dating.api.Dating_App_API
import com.application.dating.api.ServiceBuilder
import com.application.dating.main.Check_ID
import com.application.dating.model.Taikhoan
import com.application.dating.model.Taikhoan_hinhanh
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.main_profile_fragment.*
import kotlinx.android.synthetic.main.main_profile_fragment.view.*
import java.io.File

class Main_PersonalPage_Fragment : Fragment (){
    lateinit var iMyAPI : Dating_App_API
    var conpositeDisposable = CompositeDisposable()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_profile_fragment,container,false)
        iMyAPI = ServiceBuilder.getInstance().create(Dating_App_API::class.java)
        fetchdata(view)
        view.edit.setOnClickListener {
            val intent = Intent(requireContext(),Activity_Edit::class.java)
            startActivity(intent)
        }
        view.txt_comfirm.setOnClickListener {
            val intent = Intent(requireContext(),Check_ID::class.java)
            startActivity(intent)
        }
        if(MainActivity.is_xacminh!!){
            view.txt_comfirm.text = "Đã xác minh"
            view.txt_comfirm.visibility = View.INVISIBLE
        }else{
            view.txt_comfirm.text = "Xác minh"
        }
        view.txtnameandage.text = MainActivity.ten.toString()
        return view
    }
    private fun fetchdata(view : View){
        val acc = Taikhoan(
            id = MainActivity.id!!
        )
        conpositeDisposable.addAll(iMyAPI.get_avatar(acc)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({s ->
               val file : File = File(s)
            },{t :Throwable? ->
                Toast.makeText(requireContext(),t!!.message, Toast.LENGTH_SHORT).show()
            }))
        /*startActivity(Intent(this@Login_Activity, MainActivity::class.java))
        overridePendingTransition(R.anim.activity_slide_in_right,R.anim.activity_slide_out_left)*/
    }
}