package com.application.dating

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.application.dating.main.fragment.Main_Message_Fragment

import com.application.dating.main.fragment.Main_NewsFeed_Fragment
import com.application.dating.main.fragment.Main_PersonalPage_Fragment
import com.application.dating.main.fragment.Main_Tags_Fragment
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {
    companion object{
        var id: Int? = 0
        var name: String? = null
        var dateofbirth: String? = null
        var email: String? = null
        var password: String?= null
        var introduce_yourself: String? = null
        var gender: String? = null
        var latitude: Float?= 0f
        var longitude : Float? = 0f
        var gender_requirement: String? = null
        var radius: Int? = 0
        var desired_age: Int? = 0
        var height: Int? = 0
        var live_at: String? = null
        var constellation: String? = null
        var religion: String? = null
        var is_status: Boolean? = false
        var education: String?= null
        var work: String?= null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val news_feed = Main_NewsFeed_Fragment()
        val tags = Main_Tags_Fragment()
        val message = Main_Message_Fragment()
        val profile = Main_PersonalPage_Fragment()

        setCurrentFragment(news_feed)
        bottom_navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.ic_home -> setCurrentFragment(news_feed)
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