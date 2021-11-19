package com.application.dating.model

data class Account(
    val id : Int = 0,
    val name : String ?= null,
    val dateofbirth : String ?= null,
    val username : String ?= null,
    val password : String ?= null,
    val gender : String ?= null,
    val latitude : Float = 0F,
    val longitude : Float = 0F,
    val gender_requirement : String ?= null,
    val radius : Int = 0,
    val age_range : Int = 0,
    val live_at : String ?= null,
    val is_status: Boolean = false
)