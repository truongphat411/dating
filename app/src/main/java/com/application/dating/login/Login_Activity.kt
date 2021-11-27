package com.application.dating.login

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.dating.MainActivity
import com.application.dating.R
import com.application.dating.register.Register_Activity
import com.application.dating.api.Dating_App_API
import com.application.dating.api.ServiceBuilder
import com.application.dating.model.Taikhoan
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_activity.*
import com.google.gson.Gson





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
            val matkhau : String = edt_password.editText?.text.toString().trim()
            val userInfo = Taikhoan(
                email = email,
                matkhau = matkhau
            )
            conpositeDisposable.addAll(iMyAPI.LoginUser(userInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({s ->
                    if(s.contains("User not existing in Database")){
                        Toast.makeText(this@Login_Activity,"Tài khoản không tồn tại",Toast.LENGTH_SHORT).show()
                    }else if (s.contains("Wrong Password")){
                        Toast.makeText(this@Login_Activity,"Mật khẩu không đúng",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val gson = Gson()
                        val acc = gson.fromJson(s,Taikhoan::class.java)
                        MainActivity.id = acc.id
                        MainActivity.ten = acc.ten
                        MainActivity.ngaysinh = acc.ngaysinh
                        MainActivity.email = acc.email
                        MainActivity.gioitinh = acc.gioitinh
                        MainActivity.gioithieubanthan = acc.gioithieubanthan
                        MainActivity.quequan = acc.quequan
                        MainActivity.dangsongtai = acc.dangsongtai
                        MainActivity.is_trangthai = acc.is_trangthai
                        MainActivity.is_xacminh = acc.is_xacminh
                        MainActivity.bankinh = acc.bankinh
                        MainActivity.sothich = acc.sothich
                        startActivity(Intent(this@Login_Activity, MainActivity::class.java))
                        overridePendingTransition(R.anim.activity_slide_in_right,R.anim.activity_slide_out_left)
                        finish()
                    }
                },{t :Throwable? ->
                    Toast.makeText(this@Login_Activity,t!!.message,Toast.LENGTH_SHORT).show()
                }))
            /*startActivity(Intent(this@Login_Activity, MainActivity::class.java))
            overridePendingTransition(R.anim.activity_slide_in_right,R.anim.activity_slide_out_left)*/
            }
        }
    override fun onStop() {
        conpositeDisposable.clear()
        super.onStop()
    }
}
