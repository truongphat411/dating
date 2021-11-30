package com.application.dating.chat


import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.dating.R
import com.application.dating.chat.models.Chat
import com.application.dating.model.Taikhoan
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_message.*
import java.util.*

class ChatActivity : AppCompatActivity() {

    var fuser: FirebaseUser? = null
    var reference: DatabaseReference? = null

    var messageAdapter: MessageAdapter? = null
    lateinit var mChat: MutableList<Chat>

    lateinit var seenListener: ValueEventListener

    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        toolbar.setNavigationOnClickListener {
//            startActivity(Intent(this@MessageActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//        }

        // api service ntar dulu

        recycler_view.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recycler_view.layoutManager = layoutManager

        userId = intent.getStringExtra("USER_ID").toString()
        fuser = FirebaseAuth.getInstance().currentUser

        btn_send.setOnClickListener {
            val msg = text_send.text.toString()
            if (!msg.equals("")){
                sendMessage(fuser?.uid , userId, msg)
            } else {
                Toast.makeText(this, "You can't send empty message", Toast.LENGTH_SHORT).show()
            }
            // kosongkan lagi
            text_send.setText("")
        }

        // firebase
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(Taikhoan::class.java)
//                username.text = user?.username
//                if (user?.imageURL.equals("default")){
//                    profile_image.setImageResource(R.mipmap.ic_launcher)
//                } else {
//                    Glide.with(applicationContext).load(user?.imageURL).into(profile_image)
//                }
                profile_image.setImageResource(R.mipmap.ic_launcher)
                readMessages(fuser?.uid, userId, "default")
            }
        })

        seenMessage(userId)
    }

    private fun seenMessage(useId: String?) {
        reference = FirebaseDatabase.getInstance().getReference("Chats")
        seenListener = reference!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children){
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat?.receiver.equals(fuser?.uid) && chat?.sender.equals(useId)){
                        val hashMap: HashMap<String, Any> = hashMapOf()
                        hashMap.put("isseen", true)
                        snapshot.ref.updateChildren(hashMap)
                    }
                }
            }

        })
    }

    private fun readMessages(myId: String?, userId: String?, imageURL: String?) {
        mChat = arrayListOf()

        reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mChat.clear()

                for (snapshot: DataSnapshot in dataSnapshot.children){
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat?.receiver.equals(fuser?.uid) && chat?.sender.equals(userId) || chat?.receiver.equals(userId) && chat?.sender.equals(myId)){
                        mChat.add(chat!!)
                    }
                }
                // adapter
                messageAdapter = MessageAdapter(this@ChatActivity, mChat, imageURL!!)
                recycler_view.adapter = messageAdapter
            }

        })
    }

    private fun sendMessage(sender: String?, receiver: String, msg: String) {

        var reference = FirebaseDatabase.getInstance().reference

        val hashMap: HashMap<String, Any> = hashMapOf()
        sender?.let {
            hashMap.put("sender", sender)
        }
        hashMap.put("receiver", receiver)
        hashMap.put("message", msg)
        hashMap.put("isseen", true)

        reference.child("Chats").push().setValue(hashMap)

        // add user to chat fragment
        val chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
            .child(fuser?.uid!!)
            .child(userId)

        chatRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userId)
                }
            }
        })

        val chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
            .child(userId)
            .child(fuser?.uid!!)
        chatRefReceiver.child("id").setValue(fuser?.uid!!)

//        // for notification use
//        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser?.uid!!)
//        reference.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val user = dataSnapshot.getValue(User::class.java)
//                // fot notification
//            }
//
//        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item?.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)

            }
        }
    }

}
