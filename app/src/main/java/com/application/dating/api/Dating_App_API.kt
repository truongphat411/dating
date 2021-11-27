package com.application.dating.api

import com.application.dating.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface Dating_App_API{

    @POST("api/login/")
    fun LoginUser(@Body userData: Taikhoan): Observable<String>
    //////////////////////////////////////
    @POST("api/register/")
    fun RegisterUser(@Body userData: Taikhoan): Observable<String>
    ///////////////////////////////////
    @Multipart
    @POST("api/image/uploadimage")
    fun uploadImage(@Part imagepath : MultipartBody.Part,
                          @Part("id_taikhoan") id_taikhoan : RequestBody,@Part("is_anhdaidien") is_anhdaidien : RequestBody,
    ) : Call<String>
    //////////////////////////////////////
    @GET("api/account/get/{id}")
    fun getProfile(
        @Path(value = "id", encoded = false) key: Int,
    ): Call<String>
    /////////////////////////////////////
    @PATCH("api/account/updateprofile/{id}")
    fun updateprofile(@Path(value = "id", encoded = false) key: Int,@Body acc : Taikhoan): Call<String>
    ////////////////////////////////////
    @PATCH("api/account/updatesearch/{id}")
    fun updatesearch(@Path(value = "id", encoded = false) key: Int,@Body acc : Taikhoan): Call<String>
    //////////////////////////////////////
    @POST("api/image/get")
    fun get_avatar(@Body acc : Taikhoan) : Observable<String>
    ///////////////////////////////////////
    @POST("api/match/check")
    fun checkMatch(@Body ma : Taikhoan_yeuthich) : Observable<String>
    //////////////////////////////////////
    @POST("api/match/info_account")
    fun info_account(@Body userData: Taikhoan): Observable<List<Profile>>
    companion object{
        operator fun invoke() : Dating_App_API {
            return Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.1.8:8086/")
                .build()
                .create(Dating_App_API::class.java)
        }
    }
}