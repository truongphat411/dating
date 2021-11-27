package com.application.dating.model

data class Taikhoan(
    val id : Int = 0,
    val ten : String ?= null,
    val email : String ?= null,
    val ngaysinh : String ?= null,
    val gioitinh : String ?= null,
    val gioitinhyeuthich : String ?= null,
    val kinhdo : Float = 0.0F,
    val vido : Float = 0.0F,
    val bankinh : Int = 0,
    val dotuoitoithieu_yeuthich : Int = 0,
    val dotuoitoida_yeuthich : Int = 0,
    val is_xacminh : Boolean = false,
    val so_cccd : String ?= null,
    val quequan : String ?= null,
    val gioithieubanthan : String ?= null,
    val sothich : String ?= null,
    val dangsongtai : String ?= null,
    val is_trangthai : Boolean = false,
    val matkhau : String ?= null
)