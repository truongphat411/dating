package com.application.dating

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.application.dating.main.fragment.Main_Message_Fragment

import com.application.dating.main.fragment.Main_PersonalPage_Fragment
import com.application.dating.main.fragment.Main_Tags_Fragment
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {
    companion object{
        var id: Int? = 0
        var ten: String? = null
        var ngaysinh: String? = null
        var email: String? = null
        var matkhau: String?= null
        var gioithieubanthan: String? = null
        var gioitinh: String? = null
        var kinhdo: Float?= 0f
        var vido : Float? = 0f
        var gioitinhyeuthich: String? = null
        var bankinh: Int? = 0
        var dotuoitoithieu_yeuthich: Int? = 0
        var dotuoitoida_yeuthich: Int? = 0
        var dangsongtai: String? = null
        var is_trangthai: Boolean? = false
        var is_xacminh: Boolean? = false
        var so_cccd : String ?= null
        var quequan : String ?= null
        var sothich : String ?= null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val tags = Main_Tags_Fragment()
        val message = Main_Message_Fragment()
        val profile = Main_PersonalPage_Fragment()

        setCurrentFragment(tags)
        bottom_navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                //R.id.ic_home -> setCurrentFragment(news_feed)
                R.id.ic_tags -> setCurrentFragment(tags)
                R.id.ic_message -> setCurrentFragment(message)
                R.id.ic_profile -> setCurrentFragment(profile)
            }
            true
        }
    }
    private fun setCurrentFragment(fragment : Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_main,fragment)
            commit()
        }
}