package com.application.dating.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.application.dating.chat.models.Chat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*
import com.application.dating.R
import com.application.dating.chat.models.UserChat
import kotlinx.android.synthetic.main.messager_activity.*

class MessagerActivity : AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null
    lateinit var reference: DatabaseReference
    val TAG = "MTL"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.messager_activity)
        setSupportActionBar(toolbar)
//        supportActionBar?.title = "Trao đổi"


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, ChatFragment())
            commit()

            Log.d(TAG, "onCreate: ")
            firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let {
                Log.d(TAG, "onCreate: " + it.uid)
            }

            // Mendapatkan List Pengguna
            reference =
                FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser?.uid!!)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(e: DatabaseError) {
                    Toast.makeText(this@MessagerActivity, e.message, Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(UserChat::class.java)
                    //username.text = user?.username
                    //profile_image.setImageResource(R.mipmap.ic_launcher)
//                if (user?.imageURL.equals("default")){
//                    profile_image.setImageResource(R.mipmap.ic_launcher)
//                } else {
//                    //Change this
//                    Glide.with(applicationContext).load(user?.imageURL).into(profile_image)
//                }
                }
            })

            // Mendapatkan List Pesan
            reference = FirebaseDatabase.getInstance().getReference("Chats")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataError: DatabaseError) {
                    // do something if databse error
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val viewPageAdapter = ViewPagerAdapter(supportFragmentManager)
                    // get number of unreaded message
                    var unread = 0
                    for (snapshot in dataSnapshot.children) {
                        val chat = snapshot.getValue(Chat::class.java)
                        if (chat?.receiver != null && chat.receiver?.equals(firebaseUser!!.uid)!! && !chat.isseen!!) {
                            unread++
                        }
                    }
//                if (unread == 0) {
//                    viewPageAdapter.addFragment(ChatFragment(), "Chats")
//                } else {
//                    viewPageAdapter.addFragment(ChatFragment(), "($unread) Chats")
//                }

                    // Tambah tab
//                viewPageAdapter.addFragment(UsersFragment(), "Users")
//                viewPageAdapter.addFragment(ProfileFragment(), "Profile")

                    // Set adapter
//                view_pager.adapter = viewPageAdapter
//                tab_layout.setupWithViewPager(view_pager)
                }

            })
        }
    }
}