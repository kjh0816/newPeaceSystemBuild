package com.base.newPeaceSystemBuild.vo

data class Member(
    val id: Int,

    val regDate: String,
    val updateDate: String,

    val roleLevel: Int,

    val loginId: String,
    val loginPw: String,

    val name: String,
    val cellphoneNo: String,
    val email: String,

    val location: String,

    val delStatus: Int,
    val delDate: String?
)