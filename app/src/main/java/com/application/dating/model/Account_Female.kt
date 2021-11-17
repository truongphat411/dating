package com.application.dating.model

class Account_Female {
    var id: Int? = null
    var name: String? = null
    var dateofbirth: String?= null
    var username: String?= null
    var password: String?= null
    var gender: String?= null
    var latitude: Float?= 0f
    var longitude : Float? = 0f
    var gender_requirement: String?= null
    var radius: Int?= 0
    var age_range: Int?= 0
    var live_at: String?= null
    var is_status: Boolean?= false

    constructor(username: String?, password: String?) {
        this.username = username
        this.password = password
    }

    constructor(
        id: Int?,
        name: String?,
        dateofbirth: String?,
        username: String?,
        password: String?,
        gender: String?,
        latitude: Float?,
        longitude: Float?,
        gender_requirement: String?,
        radius: Int?,
        age_range: Int?,
        live_at: String?,
        is_status: Boolean?,
    ) {
        this.id = id
        this.name = name
        this.dateofbirth = dateofbirth
        this.username = username
        this.password = password
        this.gender = gender
        this.latitude = latitude
        this.longitude = longitude
        this.gender_requirement = gender_requirement
        this.radius = radius
        this.age_range = age_range
        this.live_at = live_at
        this.is_status = is_status
    }

    constructor(
        name: String?,
        dateofbirth: String?,
        username: String?,
        password: String?,
        gender: String?,
        gender_requirement: String?
    ){
        this.name = name
        this.dateofbirth = dateofbirth
        this.username = username
        this.password = password
        this.gender = gender
        this.gender_requirement = gender_requirement
    }
}