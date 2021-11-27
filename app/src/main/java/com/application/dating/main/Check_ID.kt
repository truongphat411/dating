package com.application.dating.main

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.application.dating.R
import com.application.dating.LicenseUtil.getLicense
import com.application.dating.register.Register_ViewModel
import com.application.dating.register.fragment.*
import com.regula.documentreader.api.DocumentReader
import com.regula.documentreader.api.completions.IDocumentReaderCompletion
import com.regula.documentreader.api.completions.IDocumentReaderPrepareCompletion
import com.regula.documentreader.api.enums.DocReaderAction
import com.regula.documentreader.api.enums.Scenario
import com.regula.documentreader.api.enums.eGraphicFieldType
import com.regula.documentreader.api.enums.eVisualFieldType
import com.regula.documentreader.api.errors.DocumentReaderException
import com.regula.documentreader.api.params.DocReaderConfig
import com.regula.documentreader.api.results.DocumentReaderResults
import kotlinx.android.synthetic.main.check_id_fragment.*
import kotlinx.android.synthetic.main.check_id_fragment.view.*
import org.json.JSONObject
import java.io.*
import java.util.*


class Check_ID : AppCompatActivity(){
    lateinit var viewModel: Register_ViewModel
    private var sharedPreferences: SharedPreferences? = null
    private var doRfid = false
    private var loadingDialog: AlertDialog? = null
    companion object {
        private const val REQUEST_BROWSE_PICTURE = 11
        private const val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 22
        private const val MY_SHARED_PREFS = "MySharedPrefs"
        lateinit var encodeDocumentString : String
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_id_fragment)
        sharedPreferences = getSharedPreferences(MY_SHARED_PREFS, AppCompatActivity.MODE_PRIVATE)
        initView()
    }
    override fun onPause() {
        super.onPause()
        if (loadingDialog != null) {
            loadingDialog!!.dismiss()
            loadingDialog = null
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            //Image browsing intent processed successfully
            if (requestCode == REQUEST_BROWSE_PICTURE) {
                if (data!!.data != null) {
                    val selectedImage = data.data
                    val bmp = getBitmap(selectedImage, 1920, 1080)
                    loadingDialog = showDialog("Processing image")
                    DocumentReader.Instance().recognizeImage(bmp!!, completion)
                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    //access to gallery is allowed
                    createImageBrowsingRequest()
                } else {
                    Toast.makeText(
                        this@Check_ID,
                        "Permission required, to browse images",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    private fun initView() {
        btn_choose!!.setOnClickListener { _: View? ->
            if (!DocumentReader.Instance().documentReaderIsReady) {
                val initDialog = showDialog("Vui lòng chờ....")
                //preparing database files, it will be downloaded from network only one time and stored on user device
                DocumentReader.Instance().prepareDatabase(
                    this@Check_ID,
                    "Full",
                    object : IDocumentReaderPrepareCompletion {
                        override fun onPrepareProgressChanged(progress: Int) {
                            initDialog.setTitle("Downloading database: $progress%")
                        }
                        override fun onPrepareCompleted(
                            status: Boolean,
                            error: DocumentReaderException?
                        ) {
                            if (status) {
                                initDialog.setTitle("Vui lòng chờ....")
                                initializeReader(initDialog)
                            } else {
                                initDialog.dismiss()
                                Toast.makeText(
                                    this@Check_ID,
                                    "Prepare DB failed:$error",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    })
            }
            if (!DocumentReader.Instance().documentReaderIsReady) return@setOnClickListener
            clearResults()
            //starting video processing
            DocumentReader.Instance().showScanner(this@Check_ID, completion)
        }
        btn_id.setOnClickListener {

        }
    }
    fun check(){
        var dialog = Dialog(this@Check_ID)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.show()
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            "https://api-us.faceplusplus.com/facepp/v3/compare",
            Response.Listener { response ->
                val obj = JSONObject(response.toString())
                val confidence: Float = obj.getString("confidence").toFloat()
                if (response != null) {
                    if (confidence > 70) {
                        dialog.dismiss()
                        Toast.makeText(this@Check_ID,"Hình ảnh hợp lệ",Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        dialog.dismiss()
                        Toast.makeText(this@Check_ID,"Hình ảnh không hợp lệ",Toast.LENGTH_SHORT).show()
                    }
                }
            },
            Response.ErrorListener { error ->
                dialog.dismiss()
                Toast.makeText(
                    this@Check_ID,
                    "" + error.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["api_key"] = "8NlrUwBlGYK3m8x5SMyivPzTFiA6sbjn"
                params["api_secret"] = "Z3D_LsTN1TfEDOHvQMxLwBhoAvfG6A0v"
                //params["image_base64_1"] = encodeString
                params["image_base64_2"] = encodeDocumentString
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this@Check_ID)
        requestQueue.add(stringRequest)
    }
    private fun encodeBitmapImage(bitmap: Bitmap){
        val byteArrayOutputStream : ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        val byteofimage = byteArrayOutputStream.toByteArray()
        //encodeString = android.util.Base64.encodeToString(byteofimage,Base64.DEFAULT)
    }
    private fun initializeReader(initDialog: AlertDialog) {
        val config = DocReaderConfig(getLicense(this@Check_ID))
        config.isLicenseUpdate = true

        DocumentReader.Instance().initializeReader(
            this,
            config
        ) { success, error ->
            if (initDialog.isShowing) {
                initDialog.dismiss()
                DocumentReader.Instance().showScanner(this, completion)
            }
            if (!success) { //Initialization was not successful
                Toast.makeText(this, "Init failed:$error", Toast.LENGTH_LONG).show()
                return@initializeReader
            }
            setupCustomization()
            setupFunctionality()
            DocumentReader.Instance().processParams().scenario = Scenario.SCENARIO_OCR
        }
    }
    private fun setupCustomization() {
        DocumentReader.Instance().customization().edit().setShowHelpAnimation(false).apply()
    }

    private fun setupFunctionality() {
        DocumentReader.Instance().functionality().edit().setShowCameraSwitchButton(true).apply()
    }
    //DocumentReader processing callback
    private val completion = IDocumentReaderCompletion { action, results, error ->
        //processing is finished, all results are ready
        if (action == DocReaderAction.COMPLETE) {
            if (loadingDialog != null && loadingDialog!!.isShowing) {
                loadingDialog!!.dismiss()
            }
            //Checking, if nfc chip reading should be performed
            if (doRfid && results != null && results.chipPage != 0) {
                //starting chip reading
                DocumentReader.Instance()
                    .startRFIDReader(this) { rfidAction, results, _ ->
                        if (rfidAction == DocReaderAction.COMPLETE || rfidAction == DocReaderAction.CANCEL) {
                            displayResults(results)
                        }
                    }
            } else {
                displayResults(results)
            }
        } else {
            //something happened before all results were ready
            if (action == DocReaderAction.CANCEL) {
                Toast.makeText(this, "Scanning was cancelled", Toast.LENGTH_LONG)
                    .show()
            } else if (action == DocReaderAction.ERROR) {
                Toast.makeText(this, "Error:$error", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun showDialog(msg: String): AlertDialog {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.simple_dialog, null)
        dialog.setTitle(msg)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        return dialog.show()
    }
    //show received results on the UI
    private fun displayResults(results: DocumentReaderResults?) {
        if (results != null) {
            val portrait = results.getGraphicFieldImageByType(eGraphicFieldType.GF_PORTRAIT)
            val name = results.getTextFieldValueByType(eVisualFieldType.FT_SURNAME_AND_GIVEN_NAMES)
            if (name != null && portrait != null) {
                txt_message?.visibility = View.GONE
            }else{
                txt_message?.visibility = View.VISIBLE
            }
            // through all text fields
            if (results.textResult != null) {
                for (textField in results.textResult!!.fields) {
                        val value = results.getTextFieldValueByType(textField.fieldType, textField.lcid)
                    Log.d("MainActivity", """$value """.trimIndent())
                }
            }
            var documentImage =
                results.getGraphicFieldImageByType(eGraphicFieldType.GF_DOCUMENT_IMAGE)
            if (documentImage != null) {
                val aspectRatio = documentImage.width.toDouble() / documentImage.height
                    .toDouble()
                documentImage = Bitmap.createScaledBitmap(
                    documentImage,
                    (480 * aspectRatio).toInt(),
                    480,
                    false
                )
                documentImageIv!!.setImageBitmap(documentImage)
                encodeBitmapImage(documentImage)
            }
        }
    }
    private fun clearResults() {
        documentImageIv!!.setImageResource(R.drawable.ic_id)
    }
    // creates and starts image browsing intent
    // results will be handled in onActivityResult method
    private fun createImageBrowsingRequest() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            REQUEST_BROWSE_PICTURE
        )
    }

    // loads bitmap from uri
    private fun getBitmap(selectedImage: Uri?, targetWidth: Int, targetHeight: Int): Bitmap? {
        val resolver = contentResolver
        var `is`: InputStream? = null
        try {
            `is` = resolver.openInputStream(selectedImage!!)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(`is`, null, options)

        //Re-reading the input stream to move it's pointer to start
        try {
            `is` = resolver.openInputStream(selectedImage!!)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeStream(`is`, null, options)
    }

    // see https://developer.android.com/topic/performance/graphics/load-bitmap.html
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        bitmapWidth: Int,
        bitmapHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > bitmapHeight || width > bitmapWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > bitmapHeight
                && halfWidth / inSampleSize > bitmapWidth
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}