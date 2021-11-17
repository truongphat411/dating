package com.application.dating.register.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.application.dating.R
import com.application.dating.register.Register_ViewModel
import com.application.dating.api.Dating_App_API
import com.application.dating.model.Compare_API
import com.application.dating.register.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.bottomsheet_choose_image.view.*
import kotlinx.android.synthetic.main.register_avatar_fragment.*
import kotlinx.android.synthetic.main.register_avatar_fragment.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class Register_Avatar_Fragment : Fragment() , Utils {
    lateinit var viewModel: Register_ViewModel
    private val CAMERA_PERMISSION_CODE = 123
    private val STORAGE_PERMISSION_CODE = 113
    lateinit var  my_bm: Bitmap
    lateinit var  filePath : String
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
/*            launcher.launch(
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
            )*/
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

/*    private val launcher =
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
        }*/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            STORAGE_PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }else{
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
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
        if(resultCode == Activity.RESULT_OK && requestCode == STORAGE_PERMISSION_CODE){
            val selectedImage = data?.data
            my_bm = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImage)
            imv_profile.setImageBitmap(my_bm)
            imv_ic_camera.visibility = View.GONE
        }
    }


 //   private fun checkPermission()
/*    private fun checkPermission(permission: String, requestCode: Int) {
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
    }*/

  /*  override fun onRequestPermissionsResult(
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
    }*/

    private fun check_image() {
        //Use BitmapFactory to load bitmap from Path
/*        val fullSizeBitmap1 : Bitmap = BitmapFactory.decodeFile("")
        val fullSizeBitmap2 : Bitmap = BitmapFactory.decodeFile("aaa")*/

        val bitmap1 = BitmapFactory.decodeResource(resources,R.drawable.aaaa)
        val bitmap2 = BitmapFactory.decodeResource(resources,R.drawable.bbbbbb)
        //Scale Down the Bimap
        val reducedBitmap1 : Bitmap = reduceBitmapSize(bitmap1,240000)
        val reducedBitmap2 : Bitmap = reduceBitmapSize(bitmap2,240000)


        //Save the Scaled Down Bitmap to file
        val reducedFile1 : File = bitmaptofile("img1.jpg",requireContext(),reducedBitmap1)
        val reducedFile2 : File = bitmaptofile("img2.jpg",requireContext(),reducedBitmap2)

        Log.d("haha", "check_image: "+reducedFile1.length())
        Log.d("haha", "check_image12312: "+reducedFile2.length())


        val requestBody1 : RequestBody = RequestBody.create(MediaType.parse("image/*"),reducedFile1)
        val requestBody2 : RequestBody = RequestBody.create(MediaType.parse("image/*"),reducedFile2)

        Dating_App_API("https://api-us.faceplusplus.com/facepp/").checkImage(
            MultipartBody.Part.createFormData(
                "image_file1",
                reducedFile1.name,
                requestBody1
            ),
            MultipartBody.Part.createFormData(
                "image_file2",
                reducedFile2.name,
                requestBody2
            ),
            RequestBody.create(MediaType.parse("multipart/form-data"), "8NlrUwBlGYK3m8x5SMyivPzTFiA6sbjn"),
            RequestBody.create(MediaType.parse("multipart/form-data"), "Z3D_LsTN1TfEDOHvQMxLwBhoAvfG6A0v")
        ).enqueue(object : Callback<Compare_API> {
            override fun onResponse(call: Call<Compare_API>, response: Response<Compare_API>) {
                Log.d("hihi", "onResponse: "+response.body())
            }
            override fun onFailure(call: Call<Compare_API>, t: Throwable) {
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }
    override fun bitmaptofile(filename : String,context: Context, bm: Bitmap): File {
        //create a file to write bitmap data
        val f  =  File(context.cacheDir, filename)
        f.createNewFile()

        //Convert bitmap to byte array
        val bitmap : Bitmap = bm
        val bos =  ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos)
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        var fos : FileOutputStream?= null
        try {
            fos =  FileOutputStream(f)
        } catch (e : FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos!!.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e : IOException) {
            e.printStackTrace()
        }
        return f
    }

    override fun reduceBitmapSize(bm: Bitmap, MAX_SIZE: Int): Bitmap {
        var ratioSquare : Double = 0.0
        val bitmapHeight : Int = 50
        val bitmapWidth : Int = 50
        ratioSquare = ((bitmapHeight * bitmapHeight) / MAX_SIZE).toDouble()
        if(ratioSquare <= 1)
            return bm;
        val ratio : Double = Math.sqrt(ratioSquare)
        Log.d("my log", "reduceBitmapSize: " + ratio)
        val requiredHeight : Long = Math.round(bitmapHeight / ratio)
        val requiredWidth : Long = Math.round(bitmapWidth / ratio)
        return Bitmap.createScaledBitmap(bm, 100, 100,true)
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

