package com.application.dating.register.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.application.dating.R
import com.application.dating.register.Register_ViewModel
import kotlinx.android.synthetic.main.register_email_fragment.view.*

class Register_Email_Fragment : Fragment(){
    lateinit var viewModel: Register_ViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.register_email_fragment,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(Register_ViewModel::class.java)
        viewModel.selectItem(60.0)
        view.btn_email.setOnClickListener{
            email = view.txt_username.text.toString()
            viewModel.selectItem(70.0)
            val fragment = Register_Password_Fragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.addToBackStack(null)
            transaction?.replace(R.id.frame_infomation,fragment)?.commit()
        }
        return view
    }
    companion object{
        var email : String ?= null
    }
}