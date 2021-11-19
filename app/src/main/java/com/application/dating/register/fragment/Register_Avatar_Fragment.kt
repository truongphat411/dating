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
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.application.dating.Activity_Location
import com.application.dating.Activity_Location_More
import com.application.dating.R
import com.application.dating.register.Register_Activity
import com.application.dating.register.Register_ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.bottomsheet_choose_image.view.*
import kotlinx.android.synthetic.main.register_avatar_fragment.*
import kotlinx.android.synthetic.main.register_avatar_fragment.view.*
import org.json.JSONObject
import java.io.*


class Register_Avatar_Fragment : Fragment() {
    lateinit var viewModel: Register_ViewModel
    private val CAMERA_PERMISSION_CODE = 123
    private val STORAGE_PERMISSION_CODE = 113
    private var  selectedImage : Uri? = null
    lateinit var bitmap : Bitmap
    lateinit var  filePath : String
    private var conpositeDisposable = CompositeDisposable()
    lateinit var encodeString : String
    lateinit var jsonObject : JSONObject
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
            var dialog = Dialog(requireActivity())
            dialog.setContentView(R.layout.dialog_loading)
            dialog.show()
            val stringRequest = object : StringRequest(Request.Method.POST,"https://api-us.faceplusplus.com/facepp/v3/compare",
            Response.Listener { response ->
                val obj = JSONObject(response.toString())
                val confidence : Float = obj.getString("confidence").toFloat()
                if(response != null){
                    if(confidence > 70){
                        val intent = Intent(requireContext(), Activity_Location::class.java)
                        val name : String = viewModel.name.toString()
                        intent.putExtra("name",viewModel.name.toString())
                        intent.putExtra("dateofbirth",viewModel.dateofbirth.toString())
                        intent.putExtra("username",viewModel.username.toString())
                        intent.putExtra("password",viewModel.password.toString())
                        intent.putExtra("gender",viewModel.gender.toString())
                        intent.putExtra("gender_requirement",viewModel.gender_requirement.toString())
                        startActivity(intent)
                        dialog.dismiss()
                        requireActivity().finish()
                    }else{
                        dialog.dismiss()
                        Toast.makeText(requireContext(),"Ảnh đại diện không hợp lệ",Toast.LENGTH_LONG).show()
                    }
                }
            },Response.ErrorListener {
                error ->
                    dialog.dismiss()
                    Toast.makeText(requireContext(),""+error.message.toString(),Toast.LENGTH_LONG).show()
                })
            {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String,String>()
                    params["api_key"] = "8NlrUwBlGYK3m8x5SMyivPzTFiA6sbjn"
                    params["api_secret"] = "Z3D_LsTN1TfEDOHvQMxLwBhoAvfG6A0v"
                    params["image_base64_1"] = encodeString
                    params["image_base64_2"] = Register_ID_Fragment.encodeDocumentString
                    return params
                }
            }
            val requestQueue = Volley.newRequestQueue(requireActivity())
            requestQueue.add(stringRequest)
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
                selectedImage = data?.data
                val inputStream : InputStream? = requireActivity().contentResolver.openInputStream(selectedImage!!)
                bitmap = BitmapFactory.decodeStream(inputStream)
                imv_ic_camera.visibility = View.GONE
                imv_profile.setImageBitmap(bitmap)
                encodeBitmapImage(bitmap)
        }
    }

    private fun encodeBitmapImage(bitmap: Bitmap){
        val byteArrayOutputStream : ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        val byteofimage = byteArrayOutputStream.toByteArray()
        encodeString = android.util.Base64.encodeToString(byteofimage,Base64.DEFAULT)
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

/*    override fun bitmaptofile(filename : String,context: Context, bm: Bitmap): File {
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
    }*/

/*    override fun reduceBitmapSize(bm: Bitmap, MAX_SIZE: Int): Bitmap {
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
    }*/
}

