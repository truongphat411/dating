package com.application.dating

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.application.dating.api.Dating_App_API
import com.application.dating.login.Login_Activity
import com.application.dating.model.Account
import com.google.android.gms.location.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_location.*
import java.util.*

class Activity_Location : AppCompatActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var iMyAPI : Dating_App_API
    var conpositeDisposable = CompositeDisposable()
    var PERMISSION_ID = 52
    private var latitude : Float = 0.0F
    private var longitude : Float = 0.0F
    private var live_at : String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        imv_down.setOnClickListener {
            val intent = Intent(this, Activity_Location_More::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_slide_bottom_up, R.anim.activity_slide_nothing)
            finish()
        }
        btn_turn_on_location.setOnClickListener {
            /*val intent = Intent(this@TurnOn_Location_Activity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left)
            finish()*/
            Log.d("Debug:",CheckPermission().toString())
            Log.d("Debug:",isLocationEnabled().toString())
            RequestPermission()
            getLastLocation()
        }
    }
    fun CheckPermission():Boolean{
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }
    fun RequestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }
    fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug", "You have the permission ")
            }
        }
    }
    fun getLastLocation(){
        if(CheckPermission()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                    var location:Location? = task.result
                    if(location == null){
                        NewLocationData()
                    }else{
                        var dialog = Dialog(this@Activity_Location)
                        dialog.setContentView(R.layout.dialog_loading)
                        dialog.show()
                        val intent : Intent = intent
                        val userInfo = Account(
                         name = intent.getStringExtra("name"),
                         dateofbirth =  intent.getStringExtra("dateofbirth"),
                         username =  intent.getStringExtra("username"),
                        password = intent.getStringExtra("password"),
                         gender = intent.getStringExtra("gender"),
                        latitude = location.latitude.toFloat(),
                         longitude = location.longitude.toFloat(),
                         gender_requirement = intent.getStringExtra("gender_requirement"),
                         radius = 50,
                         age_range = 30,
                         live_at = getCityName(location.latitude,location.longitude),
                         is_status = false
                        )
                        conpositeDisposable.addAll(iMyAPI.RegisterUser(userInfo)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({s ->
                                if(s.contains("Register successfully")){
                                    dialog.dismiss()
                                    startActivity(Intent(this@Activity_Location, Login_Activity::class.java))
                                    overridePendingTransition(R.anim.activity_slide_in_right,R.anim.activity_slide_out_left)
                                    Toast.makeText(this@Activity_Location,"Đăng ký thành công",Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            },{t :Throwable? ->
                                dialog.dismiss()
                                Toast.makeText(this@Activity_Location,t!!.message,Toast.LENGTH_SHORT).show()
                            }))
/*                        Toast.makeText(this@Activity_Location,"You Last Location is : Long: "+ location.longitude + " , Lat: "
                                + location.latitude + "\n" + getCityName(location.latitude,location.longitude), Toast.LENGTH_LONG).show()*/

                    }
                }
            }else{
                Toast.makeText(this,"Vui lòng bật vị trí",Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }
    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
            Toast.makeText(this@Activity_Location,"You Last Location is : Long: "+ lastLocation.longitude + " , Lat: "
                    + lastLocation.latitude + "\n" + getCityName(lastLocation.latitude,lastLocation.longitude), Toast.LENGTH_LONG).show()
        }
    }
    private fun getCityName(lat: Double,long: Double):String{
        var cityName:String = ""
        var countryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,3)
        cityName = Adress.get(0).locality
        countryName = Adress.get(0).countryName
        Log.d("Debug:","Your City: " + cityName + " ; your Country " + countryName)
        return cityName
    }
}