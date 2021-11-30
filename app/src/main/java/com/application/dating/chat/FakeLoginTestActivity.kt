package com.application.dating.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.application.dating.MainActivity
import com.application.dating.R
import com.application.dating.model.Taikhoan
import com.google.firebase.auth.FirebaseAuth

class FakeLoginTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fake_login_test)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            MainActivity.id = 1
            MainActivity.ten = "Truong Loc"
            MainActivity.email = "loc@loc.loc"

            val id = FirebaseAuthCommon.getUserIDByEmail("loc@loc.loc")
            Log.d("MTL", "onCreate: FakeLoginTestActivity" + id)
            val auth = FirebaseAuth.getInstance()
//            auth.signInWithEmailAndPassword("loc@loc.com", "123456")
//                .addOnCompleteListener {
//                    startActivity(Intent(this@FakeLoginTestActivity, MessagerActivity::class.java))
//                }
            startActivity(Intent(this@FakeLoginTestActivity, MessagerActivity::class.java))
        }
    }
}