package com.application.dating.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.application.dating.R
import com.application.dating.chat.models.Chat
import com.application.dating.chat.models.UserChat
import kotlinx.android.synthetic.main.user_item.view.*

class UserAdapter(val context: Context, val mUsers: MutableList<UserChat>, val isChat: Boolean): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    var theLastMessage: String? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mUsers.size

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user = mUsers.get(position)
//        holder.itemView.username.text = user.username
//
//        // foto profil
//        if (user.imageURL.equals("default")){
//            holder.itemView.profile_image.setImageResource(R.mipmap.ic_launcher)
//        } else {
//            Glide.with(context).load(user.imageURL).into(holder.itemView.profile_image)
//        }
        holder.itemView.profile_image.setImageResource(R.mipmap.ic_launcher)
        // apakah tampilan chat / bukan
        if (isChat){
            lastMessage(user.id.toString(), holder.itemView.last_msg)
        } else {
            holder.itemView.last_msg.visibility = View.GONE
        }
        // apakah tampilan chat / bukan
        if (isChat){
            holder.itemView.img_on.visibility = View.VISIBLE
            holder.itemView.img_off.visibility = View.GONE
//            if (user.status.equals("online")){
//                holder.itemView.img_on.visibility = View.VISIBLE
//                holder.itemView.img_off.visibility = View.GONE
//            } else {
//                holder.itemView.img_on.visibility = View.GONE
//                holder.itemView.img_off.visibility = View.VISIBLE
//            }
        } else {
            holder.itemView.img_on.visibility = View.GONE
            holder.itemView.img_off.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("USER_ID", user.id)
            context.startActivity(intent)
        }
    }

    private fun lastMessage(userid: String, last_msg: TextView?) {
        theLastMessage = "default"

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.getChildren()) {
                    val chat = snapshot.getValue(Chat::class.java)
                    if (firebaseUser != null && chat != null){
                        if (chat.receiver != null && chat.receiver?.equals(firebaseUser.uid)!! && chat.sender.equals(userid)){
                            theLastMessage = chat.message
                        }
                    }
                }

                when (theLastMessage){
                    "default" -> last_msg?.text = "No Message"
                    else -> {
                        last_msg?.text = theLastMessage
                    }
                }

                theLastMessage = "default"
            }

        })
    }

}