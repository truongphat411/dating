package com.application.dating.api

import com.application.dating.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface Dating_App_API{

    @POST("api/login/")
    fun LoginUser(@Body userData: Account): Observable<Account>

    @POST("api/register/")
    fun RegisterUser(@Body userData: Account_Male): Observable<String>

    @Multipart
    @POST("api/image/")
    fun uploadImage(@Part imagepath : MultipartBody.Part,
                    @Part("account_id") account_id : RequestBody,
    ) : Call<File_Image_Male>

    @Multipart
    @POST("v3/compare")
    fun checkImage(@Part image_file1 : MultipartBody.Part,
           @Part image_file2 : MultipartBody.Part,
           @Part("api_key") api_key : RequestBody,
           @Part("api_secret") api_secret : RequestBody
    ) : Call<Compare_API>

/*
    @GET("api/imagemale")
    fun getImageMale() : Call<File_Image_Male>
*/

/*    @GET("api/imagemale")
    val getImageMale : Observable<List<File_Image_Male>>*/

    @POST("api/imagefemale/getimagefemale")
    fun getImageMale(@Body userData: Account): Observable<List<File_Image_Male>>

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