package com.application.dating.model

data class Matches(
    val id : Int = 0,
    val female_id : Int = 0,
    val male_id : Int = 0,
    val is_female_meet : Boolean ?= false,
    val is_male_meet : Boolean ?= false,
    val is_female_match : Boolean ?= false,
    val is_male_match : Boolean ?= false
)
