package com.application.dating.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

object ServiceBuilder {
    private var instance : Retrofit?= null

    fun getInstance() : Retrofit {
        if(instance == null)
            instance = Retrofit.Builder().baseUrl("http://192.168.1.10:8086/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return instance!!
    }


    fun getCompare(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.10:8086/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}