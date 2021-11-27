package com.application.dating.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import com.google.gson.GsonBuilder

import com.google.gson.Gson
import java.util.concurrent.TimeUnit


object ServiceBuilder {
    private var instance : Retrofit?= null

    fun getInstance() : Retrofit {
        if(instance == null)
            instance = Retrofit.Builder().baseUrl("http://192.168.1.8:8086/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return instance!!
    }

     fun upload(): Dating_App_API {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.8:8086/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Dating_App_API::class.java)
    }
}