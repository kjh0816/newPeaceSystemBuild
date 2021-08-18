package com.base.newPeaceSystemBuild.vo.standard

data class Flower(
    val id: Int,
    val regDate: String,
    var updateDate: String,
    val name: String,
    var retailPrice: String,
    var standardPrice: String,
    var costPrice: String,
    val height: String,
    val width: String
)
