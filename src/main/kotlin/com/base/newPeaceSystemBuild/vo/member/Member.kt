package com.base.newPeaceSystemBuild.vo.member


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

    val delStatus: Boolean,
    val delDate: String?,

    val extra__regDate: String?,
    val extra__updateDate: String?,
    val extra__introduce: String?,
    val extra__authenticationLevel: Int?,
    val extra__authenticationDate: String?,
    val extra__roleCategoryId: Int?,
    val extra__roleName: String?
) {
    // MyBatis 통해 DB에서 값을 할당받는것이 아닌 추후에 썸네일 표시할때 주입받기때문에 매개변수가 아닌 필드에 선언
    // 기본적으로 썸네일로 표실될 때 주입받지 않으면 null
    var extra__thumbnailImgUrl: String? = null
}