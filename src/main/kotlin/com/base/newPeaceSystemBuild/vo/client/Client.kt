package com.base.newPeaceSystemBuild.vo.client

data class Client(
    val id: Int,
    val regDate: String,
    val updateDate: String,
    val deceasedName: String,
    val relatedName: String,
    val cellphoneNo: String,
    val location: String,
    val address: String
)