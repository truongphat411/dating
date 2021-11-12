package com.application.dating.model

class Account {
    var id: Int? = null
    var name: String? = null
    var dateofbirth: String?= null
    var email: String?= null
    var password: String?= null
    var introduce_yourself: String?= null
    var gender: String?= null
    var latitude: Float?= 0f
    var longitude : Float? = 0f
    var gender_requirement: String?= null
    var radius: Int?= 0
    var desired_age: Int?= 0
    var height: Int?= 0
    var live_at: String?= null
    var constellation: String?= null
    var religion: String?= null
    var is_status: Boolean?= false
    var education: String?= null
    var work: String?= null

    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }

    constructor(
        id: Int?,
        name: String?,
        dateofbirth: String?,
        email: String?,
        password: String?,
        introduce_yourself: String?,
        gender: String?,
        latitude: Float?,
        longitude: Float?,
        gender_requirement: String?,
        radius: Int?,
        desired_age: Int?,
        height: Int?,
        live_at: String?,
        constellation: String?,
        religion: String?,
        is_status: Boolean?,
        education: String?,
        work: String?
    ) {
        this.id = id
        this.name = name
        this.dateofbirth = dateofbirth
        this.email = email
        this.password = password
        this.introduce_yourself = introduce_yourself
        this.gender = gender
        this.latitude = latitude
        this.longitude = longitude
        this.gender_requirement = gender_requirement
        this.radius = radius
        this.desired_age = desired_age
        this.height = height
        this.live_at = live_at
        this.constellation = constellation
        this.religion = religion
        this.is_status = is_status
        this.education = education
        this.work = work
    }

    constructor(
        name: String?,
        dateofbirth: String?,
        email: String?,
        password: String?,
        gender: String?,
        gender_requirement: String?
    ){
        this.name = name
        this.dateofbirth = dateofbirth
        this.email = email
        this.password = password
        this.gender = gender
        this.gender_requirement = gender_requirement
    }
}