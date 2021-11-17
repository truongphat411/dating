package com.application.dating.main.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.application.dating.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.main_profile_fragment.view.*

class Main_PersonalPage_Fragment : Fragment (){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_profile_fragment,container,false)
        return view
    }
}