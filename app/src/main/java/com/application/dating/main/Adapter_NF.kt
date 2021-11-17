package com.application.dating.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.dating.R

internal class Adapter_NF (private var itemsList: List<String>) : RecyclerView.Adapter<Adapter_NF.MyViewHolder>(){
    internal class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var imv_profile: ImageView = view.findViewById(R.id.imv_profile)
        var txt_name : TextView = view.findViewById(R.id.txt_name)
        var txt_time : TextView = view.findViewById(R.id.txt_time)
        var imv_more: ImageView = view.findViewById(R.id.imv_more)
        var txt_status : TextView = view.findViewById(R.id.txt_status)
        var imv_status :ImageView = view.findViewById(R.id.imv_status)
        var imv_favorite :ImageView = view.findViewById(R.id.imv_favorite)
        var txt_favorite : TextView = view.findViewById(R.id.txt_favorite)
        var imv_cmt : ImageView = view.findViewById(R.id.imv_cmt)
        var txt_cmt : TextView = view.findViewById(R.id.txt_cmt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_nf, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}