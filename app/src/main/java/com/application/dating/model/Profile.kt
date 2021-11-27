package com.application.dating.model

data class Profile(
    val id : Int = 0,
    val ten : String ?= null,
    val tuoi : Int = 0,
    val duongdan : String ?= null,
    val gioitinh : String ?= null,
    val is_trangthai : Boolean = false,
    val gioithieubanthan : String ?= null,
    val dangsongtai : String ?= null
)
