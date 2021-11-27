package com.application.dating.model

data class Taikhoan_hinhanh(
    val id : Int = 0,
    val id_taikhoan : Int = 0,
    val duongdan : String ?= null,
    val ngaykhoitao : String ?= null,
    val is_anhdaidien : Boolean = false
)
