package com.base.newPeaceSystemBuild.vo.standard

data class Portrait(
    val id: Int,
    val regDate: String,
    var updateDate: String,
    val name: String,
    var retailPrice: String,
    var standardPrice: String,
    var costPrice: String
)
