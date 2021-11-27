package com.application.dating.register

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.dating.R
import com.application.dating.register.fragment.Register_Name_Fragment
import kotlinx.android.synthetic.main.register_activity.*

class Register_Activity : AppCompatActivity(){
    lateinit var viewModel: Register_ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContentView(R.layout.register_activity)
        viewModel = ViewModelProvider(this@Register_Activity).get(Register_ViewModel::class.java)
        viewModel.selectItem.observe(this , {
            animate_progress_bar.setProgressPercentage(it,true)
        })
        supportFragmentManager.beginTransaction().add(R.id.frame_infomation,
            Register_Name_Fragment()
        ).commit()
    }
}