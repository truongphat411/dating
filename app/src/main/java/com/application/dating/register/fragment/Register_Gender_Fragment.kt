package com.application.dating.register.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.application.dating.R
import com.application.dating.register.Register_ViewModel
import com.application.dating.api.Dating_App_API
import com.application.dating.api.ServiceBuilder
import kotlinx.android.synthetic.main.register_gender_fragment.view.*

class Register_Gender_Fragment : Fragment() {
    private lateinit var viewModel: Register_ViewModel
    lateinit var iMyAPI : Dating_App_API
    companion object {
        var gender : String? = null
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.register_gender_fragment,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(Register_ViewModel::class.java)
        iMyAPI = ServiceBuilder.getInstance().create(Dating_App_API::class.java)
        viewModel.selectItem(40.0)
        view.txt_gender_male.setOnClickListener {
            gender = view.txt_gender_male.text.toString().trim()
            it.background = ContextCompat.getDrawable(it.context, R.drawable.custom_bg_textview_pressed)
            view.txt_gender_female.background = ContextCompat.getDrawable(view.txt_gender_female.context, R.drawable.custom_bg_textview)
        }
        view.txt_gender_female.setOnClickListener {
            gender = view.txt_gender_female.text.toString().trim()
            it.background = ContextCompat.getDrawable(it.context, R.drawable.custom_bg_textview_pressed)
            view.txt_gender_male.background = ContextCompat.getDrawable(view.txt_gender_male.context, R.drawable.custom_bg_textview)
        }
        view.btn_gender.setOnClickListener {
            viewModel.selectItem(60.0)
            val fragment = Register_Email_Fragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.addToBackStack(null)
            transaction?.replace(R.id.frame_infomation,fragment)?.commit()
        }
        return view
    }
}