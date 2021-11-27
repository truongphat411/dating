package com.application.dating

import android.app.Dialog
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.dating.api.Dating_App_API
import com.application.dating.model.Taikhoan
import com.google.gson.Gson
import com.mohammedalaa.seekbar.DoubleValueSeekBarView
import com.mohammedalaa.seekbar.OnDoubleValueSeekBarChangeListener
import kotlinx.android.synthetic.main.activity_setting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class Activity_Setting : AppCompatActivity() {
    companion object {
        var endpoint_radius = 0
        var endpoint_agemin = 0
        var endpoint_agemax = 0
        var gender_requirement: String? = null
        var vido  = 0.0F
        var kinhdo = 0.0F
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        //getMyData()

        endpoint_radius = distance.progress
        endpoint_agemin = age_min.text.toString().toInt()
        endpoint_agemax = age_max.text.toString().toInt()
        gender_requirement = gender_text.text.toString()

        back.setOnClickListener {
            var dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_loading)
            dialog.show()
            updateData(dialog)
        }
        switch_man.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switch_man.isChecked = true
                switch_woman.isChecked = false
                switch_two.isChecked = false
                gender_text.text = "nam"
                gender_requirement = "nam"
            }
        })
        switch_woman.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switch_man.isChecked = false
                switch_woman.isChecked = true
                switch_two.isChecked = false
                gender_text.text = "nữ"
                gender_requirement = "nữ"
            }
        }
        switch_two.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switch_man.isChecked = false
                switch_woman.isChecked = false
                switch_two.isChecked = true
                gender_text.text = "cả hai"
                gender_requirement = "cả hai"
            }
        }
        distance.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                distance_text.text = p1.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (p0 != null) {
                    endpoint_radius = distance_text.text.toString().toInt()
                }
            }
        })
        double_range_seekbar.setOnRangeSeekBarViewChangeListener(object :
            OnDoubleValueSeekBarChangeListener {
            override fun onValueChanged(
                seekBar: DoubleValueSeekBarView?,
                min: Int,
                max: Int,
                fromUser: Boolean
            ) {
                age_min.text = min.toString()
                age_max.text = max.toString()
            }

            override fun onStartTrackingTouch(
                seekBar: DoubleValueSeekBarView?,
                min: Int,
                max: Int
            ) {
            }

            override fun onStopTrackingTouch(seekBar: DoubleValueSeekBarView?, min: Int, max: Int) {
                if (seekBar != null) {
                    endpoint_agemin = age_min.text.toString().toInt()
                    endpoint_agemax = age_max.text.toString().toInt()
                }
            }
        })
    }

    /*private fun getMyData(){
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("http://192.168.1.8:8086/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Dating_App_API::class.java)
        if(MainActivity.gender.equals("nam")){
            val retrofitData =  retrofitBuilder.getDataMale(MainActivity.id!!)
            retrofitData.enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                        val gson = Gson()
                        val acc = gson.fromJson(response.body(),Taikhoan::class.java)
                        distance.progress = acc.radius
                        rangeSeekbar.progress = acc.age_range
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@Activity_Setting,t.message,Toast.LENGTH_SHORT).show()
                }
            })
        }else {
            val retrofitData = retrofitBuilder.getDataFemale(MainActivity.id!!)
            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val gson = Gson()
                    val acc = gson.fromJson(response.body(),Taikhoan::class.java)
                    distance.progress = acc.radius
                    rangeSeekbar.progress = acc.age_range
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@Activity_Setting, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }*/
    private fun updateData(dialog: Dialog) {
        val intent = intent
        vido  = intent.getDoubleExtra("vido",0.0).toFloat()
        kinhdo = intent.getDoubleExtra("kinhdo",0.0).toFloat()
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("http://192.168.1.8:8086/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Dating_App_API::class.java)
        val acc = Taikhoan(
            id = MainActivity.id!!,
            gioitinh = MainActivity.gioitinh,
            gioitinhyeuthich = gender_requirement,
            bankinh = endpoint_radius,
            dotuoitoithieu_yeuthich = endpoint_agemin,
            dotuoitoida_yeuthich = endpoint_agemax,
            vido = vido,
            kinhdo = kinhdo,
            is_trangthai = true
        )
        val retrofitData = retrofitBuilder.updatesearch(MainActivity.id!!, acc)
        retrofitData.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                dialog.dismiss()
                finish()
                Toast.makeText(this@Activity_Setting, "thành công", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                dialog.dismiss()
                finish()
                Toast.makeText(this@Activity_Setting, "thất bại", Toast.LENGTH_SHORT).show()
            }
        })
    }
}