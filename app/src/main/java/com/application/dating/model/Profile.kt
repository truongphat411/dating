package com.application.dating.model

data class Profile(
    val id : Int = 0,
    val name : String ?= null,
    val age : Int = 0,
    val imagepath : String ?= null,
    val gender : String ?= null,
    val is_status : Boolean = false,
    val introduce : String ?= null,
    val location : Float = 0.0F,
    val live_at : String ?= null
)
