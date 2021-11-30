package com.application.dating.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import com.application.dating.R
import com.application.dating.chat.models.Chatlist
import com.application.dating.chat.models.Token
import com.application.dating.model.Taikhoan
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {
    lateinit var userAdapter: UserAdapter
    lateinit var mUsers: ArrayList<Taikhoan>

    var firebaseUser: FirebaseUser? = null
    lateinit var reference: DatabaseReference

    lateinit var usersList: ArrayList<Chatlist>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setup the view
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(context)

        // get users
        firebaseUser = FirebaseAuth.getInstance().currentUser
        usersList = arrayListOf()

        // get chats
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser?.uid!!)
        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usersList?.clear()
                // put chats data to variable
                for (snapshot in dataSnapshot.children){
                    val chatlist = snapshot.getValue(Chatlist::class.java)
                    usersList.add(chatlist!!)
                }

                chatList()
            }

        })

        updateToken( FirebaseInstallations.getInstance().id.toString())
    }

    private fun updateToken(token: String) {
        val reference = FirebaseDatabase.getInstance().getReference("Tokens")
        val token1 = Token(token)
        reference.child(firebaseUser?.uid!!).setValue(token1)
    }

    // get data chats
    private fun chatList(){
        mUsers = arrayListOf()
        reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUsers?.clear()
                // ambil data semua chat
                for (snapshot in dataSnapshot.children){
                    val user = snapshot.getValue(Taikhoan::class.java)

                    // mencari data chat yang ada uid current user
                    for (chatlist in usersList){
                        if (user?.id!!.equals(chatlist.id)){
                            mUsers.add(user!!)
                        }
                    }

                }
                userAdapter = UserAdapter(context!!, mUsers, true)
                recycler_view.adapter = userAdapter
            }

        })
    }
}