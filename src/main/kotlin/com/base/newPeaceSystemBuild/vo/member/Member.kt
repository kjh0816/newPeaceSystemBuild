package com.base.newPeaceSystemBuild.vo.member

import com.base.newPeaceSystemBuild.vo.GenFile

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

    val bank: String,
    val accountNum: String,

    val requestStatus: Boolean,

    val delStatus: Int,
    val delDate: String?,

    val extra__regDate: String?,
    val extra__updateDate: String?,
    val extra__introduce: String?,
    val extra__authenticationLevel: Int?,
    val extra__authenticationDate: String?,
    val extra__roleName: String?
) {
    var extra__thumbnailImgUrl: String? = null
}