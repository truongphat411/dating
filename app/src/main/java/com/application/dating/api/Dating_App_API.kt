package com.application.dating.api

import com.application.dating.model.Account
import com.application.dating.model.Compare_API
import com.application.dating.model.File_Image
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Dating_App_API{
    @POST("api/login/")
    fun LoginUser(@Body userData: Account): Observable<Account>

    @POST("api/register/")
    fun RegisterUser(@Body userData: Account): Observable<String>

    @Multipart
    @POST("api/image/")
    fun uploadImage(@Part imagepath : MultipartBody.Part,
                    @Part("account_id") account_id : RequestBody,
    ) : Call<File_Image>

    @Multipart
    @POST("v3/compare")
    fun checkImage(@Part image_file1 : MultipartBody.Part,
           @Part image_file2 : MultipartBody.Part,
           @Part("api_key") api_key : RequestBody,
           @Part("api_secret") api_secret : RequestBody
    ) : Call<Compare_API>

    companion object{
        operator fun invoke(baseUrl : String) : Dating_App_API {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Dating_App_API::class.java)
        }
    }

}