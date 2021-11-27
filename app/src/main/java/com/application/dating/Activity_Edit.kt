package com.application.dating

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.application.dating.api.Dating_App_API
import com.application.dating.model.Taikhoan
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.register_gender_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class Activity_Edit : AppCompatActivity() {
    lateinit var gender : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
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
}