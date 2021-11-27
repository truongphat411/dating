package com.application.dating.register.fragment

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.application.dating.MainActivity
import com.application.dating.R
import com.application.dating.api.Dating_App_API
import com.application.dating.api.ServiceBuilder
import com.application.dating.model.Taikhoan
import com.application.dating.register.Register_ViewModel
import com.application.dating.register.UploadRequestBody
import com.application.dating.register.getFileName
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bottomsheet_choose_image.view.*
import kotlinx.android.synthetic.main.register_avatar_fragment.*
import kotlinx.android.synthetic.main.register_avatar_fragment.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.*


class Register_Avatar_Fragment : Fragment(),UploadRequestBody.UploadCallback{
    lateinit var viewModel: Register_ViewModel
    private val CAMERA_PERMISSION_CODE = 123
    private val STORAGE_PERMISSION_CODE = 113
    lateinit var bitmap : Bitmap
    lateinit var  filePath : String
    lateinit var iMyAPI : Dating_App_API
    private var conpositeDisposable = CompositeDisposable()
    lateinit var encodeString : String
    lateinit var jsonObject : JSONObject
    lateinit var propress : ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.register_avatar_fragment, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(Register_ViewModel::class.java)
        viewModel.selectItem(100.0)
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val view2 = layoutInflater.inflate(R.layout.bottomsheet_choose_image, null)
        iMyAPI = ServiceBuilder.getInstance().create(Dating_App_API::class.java)
        propress = view.findViewById(R.id.progress_bar)
        bottomSheetDialog.setContentView(view2)
        view.imv_profile.setOnClickListener {
            bottomSheetDialog.show()
            bottomSheetDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        view.btn_image.setOnClickListener {
            check()
        }
        view2.ll_choose_camera.setOnClickListener {
/*            launcher.launch(
                ImagePicker.with(requireActivity())
                    .cameraOnly()
                    .crop(1f, 1f)
                    .cropOval()
                    .maxResultSize(1080, 1080)
                    .createIntent()
            )*/
            bottomSheetDialog.dismiss()
        }
        view2.ll_choose_gallery.setOnClickListener {
                if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {
                        // permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions,STORAGE_PERMISSION_CODE)
                }else{
                    pickImageFromGallery()
                }
            bottomSheetDialog.dismiss()
        }
        return view
    }

    private fun check() {
        var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_loading)
        dialog.show()
        val userInfo = Taikhoan(
            ten = Register_Name_Fragment.name,
            matkhau = Register_Password_Fragment.password,
            email = Register_Email_Fragment.email,
            gioitinh = Register_Gender_Fragment.gender,
            ngaysinh = Register_DateOfBirth_Fragment.dateofbirth
                    )
            conpositeDisposable.addAll(iMyAPI.RegisterUser(userInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({s ->
                    if(s.contains("User is existing in Database")){
                        Toast.makeText(requireContext(),"Tài khoản đã tồn tại",Toast.LENGTH_SHORT).show()
                    }else{
                        val gson = Gson()
                        val acc = gson.fromJson(s, Taikhoan::class.java)
                        uploadImage(selectedImage!!,acc,dialog)
                    }
                },{t :Throwable? ->
                    dialog.dismiss()
                    Toast.makeText(requireContext(),t!!.message,Toast.LENGTH_SHORT).show()
        }))
    }
    private fun uploadImage(selectedImage : Uri,tk : Taikhoan,dialog: Dialog) {
        val parcelFileDescriptor =
            requireContext().contentResolver.openFileDescriptor(selectedImage, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(selectedImage)
        )
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file, "image", this)
            Dating_App_API().uploadImage(
                MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    body
                ),
                RequestBody.create(MediaType.parse("text/plain"), tk.id.toString()),
                RequestBody.create(MediaType.parse("text/plain"), "true")
            ).enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    dialog.dismiss()
                    propress.progress = 0
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<String>,
                    response: retrofit2.Response<String>
                )
                {
                    propress.progress = 100
                    dialog.dismiss()
                    MainActivity.id = tk.id
                    MainActivity.ten = tk.ten
                    MainActivity.ngaysinh = tk.ngaysinh
                    MainActivity.email = tk.email
                    MainActivity.gioitinh = tk.gioitinh
                    MainActivity.gioithieubanthan = tk.gioithieubanthan
                    MainActivity.quequan = tk.quequan
                    MainActivity.dangsongtai = tk.dangsongtai
                    MainActivity.is_trangthai = tk.is_trangthai
                    MainActivity.is_xacminh = tk.is_xacminh
                    MainActivity.sothich = tk.sothich
                    val intent = Intent(requireContext(),MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(requireContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
            })
    }
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            when (requestCode) {
                STORAGE_PERMISSION_CODE -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        pickImageFromGallery()
                    } else {
                        Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        }
    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,STORAGE_PERMISSION_CODE)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode,requestCode,data)
        if(resultCode == Activity.RESULT_OK && requestCode == STORAGE_PERMISSION_CODE) {
            selectedImage = data?.data
            val inputStream: InputStream? =
                requireActivity().contentResolver.openInputStream(selectedImage!!)
            bitmap = BitmapFactory.decodeStream(inputStream)
            imv_ic_camera.visibility = View.GONE
            imv_profile.setImageBitmap(bitmap)
            //////////////////////////////////
        }
    }
    companion object{
     var  selectedImage : Uri? = null
    }

    override fun onProgressUpdate(percentage: Int) {
    }
}

