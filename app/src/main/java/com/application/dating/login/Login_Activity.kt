package com.application.dating.login

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.dating.MainActivity
import com.application.dating.model.Account
import com.application.dating.R
import com.application.dating.register.Register_Activity
import com.application.dating.api.Dating_App_API
import com.application.dating.api.ServiceBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_activity.*


class Login_Activity : AppCompatActivity() {
    lateinit var iMyAPI : Dating_App_API
    var conpositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContentView(R.layout.login_activity)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        iMyAPI = ServiceBuilder.getInstance().create(Dating_App_API::class.java)
        txt_register.setOnClickListener {
            startActivity(Intent(this@Login_Activity,Register_Activity::class.java))
            overridePendingTransition(R.anim.activity_slide_in_right,R.anim.activity_slide_out_left)
        }
        btn_login.setOnClickListener{
            val email : String = edt_email.editText?.text.toString().trim()
            val password : String = edt_password.editText?.text.toString().trim()
            val userInfo = Account(
                email = email,
                password = password,
            )
            conpositeDisposable.addAll(iMyAPI.LoginUser(userInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({s ->
                    startActivity(Intent(this@Login_Activity, MainActivity::class.java))
                    overridePendingTransition(R.anim.activity_slide_in_right,R.anim.activity_slide_out_left)
                    var id: Int? = s.id
                    //MainActivity.name = s.name
                    var dateofbirth: String?= s.dateofbirth
                    var email: String?= s.email
                    var password: String?= s.password
                    var introduce_yourself: String?= s.introduce_yourself
                    var gender: String?= s.gender
                    var latitude: Float?= s.latitude
                    var longitude : Float? = s.longitude
                    var gender_requirement: String?= s.gender_requirement
                    var radius: Int?= s.radius
                    var desired_age: Int?= s.desired_age
                    var height: Int?= s.height
                    var live_at: String?= s.live_at
                    var constellation: String?= s.constellation
                    var religion: String?= s.religion
                    var is_status: Boolean?= s.is_status
                    var education: String?= s.education
                    var work: String?= s.work

                },{t :Throwable? ->
                    Toast.makeText(this@Login_Activity,t!!.message,Toast.LENGTH_SHORT).show()
                }))

            }

        }
    override fun onStop() {
        conpositeDisposable.clear()
        super.onStop()
    }
}
