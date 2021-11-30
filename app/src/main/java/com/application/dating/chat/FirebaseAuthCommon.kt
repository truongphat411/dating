package com.application.dating.chat

import android.content.Intent
import android.widget.Toast
import com.application.dating.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseAuthCommon {

    companion object {

        fun register(txt_email: String, txt_password: String) {
            val auth = FirebaseAuth.getInstance()
            var reference: DatabaseReference
            auth.createUserWithEmailAndPassword(txt_email, txt_password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        assert(firebaseUser != null)
                        val userId = firebaseUser?.uid.toString()

                        reference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(userId)

                        val hasMap: HashMap<String, String> = hashMapOf()
                        hasMap.put("id", userId)
                        hasMap.put("username", txt_email)
                        hasMap.put("imaeURL", "default")
                        hasMap.put("status", "offline")
                        hasMap.put("search", txt_email.toLowerCase())
                        //input database
                    reference.setValue(hasMap).addOnCompleteListener {
                        if (it.isSuccessful){
//                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
//                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                            startActivity(intent)
//                            finish()
                        }
                    }
                    } else {
                        it.exception.toString()
                        val failedMsg = it.exception?.message

                        //Toast.makeText(this, failedMsg, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }


}