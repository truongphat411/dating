package com.application.dating.register.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.application.dating.R
import com.application.dating.register.Register_Activity
import com.application.dating.register.Register_ViewModel
import kotlinx.android.synthetic.main.register_dateofbirth_fragment.view.*
import kotlinx.android.synthetic.main.register_name_fragment.view.*

class Register_DateOfBirth_Fragment : Fragment (){
    var txtdate : String? = null
    lateinit var viewModel: Register_ViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.register_dateofbirth_fragment,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(Register_ViewModel::class.java)
        viewModel.selectItem(20.0)
        view.btn_day_of_birth.setOnClickListener{
            txtdate = view.edt_year.text.toString() + "/" + view.edt_month.text.toString() + "/" + view.edt_day.text.toString()
            viewModel.dateofbirth(txtdate.toString())
            viewModel.selectItem(40.0)
            val fragment = Register_Gender_Fragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.addToBackStack(null)
            transaction?.replace(R.id.frame_infomation,fragment)?.commit()
        }
        return view
    }
}