package com.application.dating

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.application.dating.chat.FakeLoginTestActivity
import com.application.dating.login.Login_Activity
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.splash_activity.*


class SplashActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        FirebaseApp.initializeApp(this)

        motionlayout!!.addTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                startActivity(Intent(this@SplashActivity, Login_Activity::class.java))
//                startActivity(Intent(this@SplashActivity, FakeLoginTestActivity::class.java))
                overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left)
                finish()
            }
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}
        })
    }
}