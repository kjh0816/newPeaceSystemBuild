package com.base.newPeaceSystemBuild.vo.vendor

data class CoffinOrder(
    val id: Int,
    val regDate: String,
    val updateDate: String,
    val funeralId: Int,
    val memberId: Int,
    val completionStatus: Boolean
)