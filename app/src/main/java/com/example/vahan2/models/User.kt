package com.example.vahan2.models

data class User(
    val uid: String = "",
    val name: String? = "",
    val mail: String? = null,
    val phoneNo: String? = "null",
    val address: String? = "null",

    val expertImage: String = "",
    val expertOccup: String = "null",
    val expertLang: String = "null",
    val expertExp: String = "null",

    val vehicleModleNo: String = "null",
    val vehicleRegYear: String = "null",
    val vehicleName: String = "null",
    val rtoNo: String = "null",
    val distanceTraveled: String = "null",

    @field:JvmField
    val isExpert: Boolean = false
    )

