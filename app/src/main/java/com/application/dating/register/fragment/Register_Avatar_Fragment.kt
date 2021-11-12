package com.application.dating.register.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.application.dating.model.File_Image
import com.application.dating.R
import com.application.dating.register.Register_ViewModel
import com.application.dating.register.repository.UploadRequestBody
import com.application.dating.register.repository.getFilename
import com.application.dating.api.Dating_App_API
import com.application.dating.api.ServiceBuilder
import com.application.dating.model.Compare_API
import com.fevziomurtekin.customprogress.Dialog
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.bottomsheet_choose_image.view.*
import kotlinx.android.synthetic.main.register_avatar_fragment.*
import kotlinx.android.synthetic.main.register_avatar_fragment.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class Register_Avatar_Fragment : Fragment() , UploadRequestBody.UploadCallback {
    lateinit var viewModel: Register_ViewModel
    private val CAMERA_PERMISSION_CODE = 123
    private val STORAGE_PERMISSION_CODE = 113
    private var selectedImage: Uri? = null
    private var conpositeDisposable = CompositeDisposable()
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
        bottomSheetDialog.setContentView(view2)
        view.imv_profile.setOnClickListener {
            bottomSheetDialog.show()
            bottomSheetDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        view.btn_image.setOnClickListener {
            // uploadFile()
            check_image()
        }
        view2.ll_choose_camera.setOnClickListener {
            launcher.launch(
                ImagePicker.with(requireActivity())
                    .cameraOnly()
                    .crop(1f, 1f)
                    .cropOval()
                    .maxResultSize(1080, 1080)
                    .createIntent()
            )
            bottomSheetDialog.dismiss()
        }
        view2.ll_choose_gallery.setOnClickListener {
            launcher.launch(
                ImagePicker.with(requireActivity())
                    .galleryOnly()
                    .galleryMimeTypes(  //Exclude gif images
                        mimeTypes = arrayOf(
                            "image/png",
                            "image/jpg",
                            "image/jpeg"
                        )
                    )
                    .crop(1f, 1f)
                    .cropOval()
                    .maxResultSize(1080, 1080)
                    .createIntent()
            )
            bottomSheetDialog.dismiss()
        }
        return view
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                selectedImage = it.data?.data
                imv_profile.setImageURI(selectedImage)
                imv_ic_camera.visibility = View.GONE
            } else if (it.resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(it.data), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        } else {
            Toast.makeText(requireContext(), "Permission Granted already", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Camera Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Camera Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Storage Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Storage Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun check_image() {
        if (selectedImage == null) {
            return
        }
        val parcelFileDescriptor =
            requireContext().contentResolver?.openFileDescriptor(selectedImage!!, "r", null)
                ?: return

        val parcelFileDescriptor1 =requireContext().contentResolver?.openFileDescriptor(selectedImage!!, "r", null)
                ?: return

        progress_bar.progress = 0

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(requireContext().cacheDir, getFilename(requireContext().contentResolver, selectedImage!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val inputStream1 = FileInputStream(parcelFileDescriptor1.fileDescriptor)
        val file1 = File(requireContext().cacheDir, getFilename(requireContext().contentResolver, selectedImage!!))
        val outputStream1 = FileOutputStream(file1)
        inputStream1.copyTo(outputStream1)


        val body = UploadRequestBody(file, "image", this)
        val body1 = UploadRequestBody(file1, "image", this)

        Dating_App_API("https://api-us.faceplusplus.com/facepp/").checkImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            MultipartBody.Part.createFormData(
                "image1",
                file1.name,
                body1
            ),
            RequestBody.create(MediaType.parse("multipart/form-data"), "8NlrUwBlGYK3m8x5SMyivPzTFiA6sbjn"),
            RequestBody.create(MediaType.parse("multipart/form-data"), "Z3D_LsTN1TfEDOHvQMxLwBhoAvfG6A0v")
        ).enqueue(object : Callback<Compare_API> {
            override fun onResponse(call: Call<Compare_API>, response: Response<Compare_API>) {
                progress_bar.progress = 100
                var ca : Compare_API? = response.body()
                if(ca!=null){
                    Toast.makeText(requireContext(),"Phần trăm là:"+response.body()?.confidence,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),"...",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Compare_API>, t: Throwable) {
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun uploadFile() {
        if (selectedImage == null) {
            Toast.makeText(requireContext(),"Thất bại",Toast.LENGTH_SHORT).show()
            return
        }
        val parcelFileDescriptor = requireContext().contentResolver?.openFileDescriptor(selectedImage!!, "r", null) ?: return

        progress_bar.progress = 0

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(requireContext().cacheDir, getFilename(requireContext().contentResolver,selectedImage!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

       /* val body = UploadRequestBody(file, "image",this)
        Dating_App_API().uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create(MediaType.parse("multipart/form-data"), "28")
        ).enqueue(object : Callback<File_Image> {
            override fun onFailure(call: Call<File_Image>, t: Throwable) {
                progress_bar.progress = 0
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<File_Image>, response: Response<File_Image>
            ) {
                response.body()?.let {
                    progress_bar.progress = 100
                    Toast.makeText(requireContext(),"Thành công",Toast.LENGTH_SHORT).show()
                }

            }
        })*/
    }
    override fun onProgressUpdate(percentage: Int) {
        progress_bar.progress = percentage
    }
    /*    if (text_gender!!.isNotEmpty() && text_gender_requirement!!.isNotEmpty()){
                val userInfo = Account(Infomation_Activity.register_name,
                    Infomation_Activity.register_dateofbirth,
                    Infomation_Activity.register_email,
                    Infomation_Activity.register_password,
                    text_gender,
                    text_gender_requirement
                )
                conpositeDisposable.addAll(iMyAPI.RegisterUser(userInfo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({s ->
                        if(s.contains("successfully")){
                            startActivity(Intent(requireActivity(),MainActivity::class.java))
                            }
                        Toast.makeText(requireActivity(),s,Toast.LENGTH_SHORT).show()
                        }, { t:Throwable? ->
                        Toast.makeText(requireActivity(),t!!.message,Toast.LENGTH_SHORT).show()
                        }
                    ))
            }else
            {
                Toast.makeText(requireActivity(),"Vui lòng chọn đầy đủ thông tin",Toast.LENGTH_SHORT).show()
            }*/
}
