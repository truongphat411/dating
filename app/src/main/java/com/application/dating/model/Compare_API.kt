package com.application.dating.model

import com.google.gson.annotations.SerializedName

data class Compare_API(
    val request_id : String,
    val confidence : Double,
    val face_token1 : String,
    val face_token2 : String,
)


