package com.base.newPeaceSystemBuild.vo.client

data class Client(
    val id: Int,
    val regDate: String,
    val updateDate: String,
    val memberId: Int,
    val directorMemberId: Int,
    val location: String,
    // 상주 관련 정보
    val cellphoneNo: String,
    val relatedName: String,
    val relatedAddress: String,
    val bank: String,
    val accountNum: String,
    val accountOwner: String,
    // 고인 관련 정보
    val deceasedName: String,
    val deceasedAddress: String,
    val deceasedDate: String,
    val deceasedTime: String,
    val sex: Boolean,
    val birth: String,
    val lunar: Boolean,
    val funeralHall: String,
    val funeralHallRoom: String,
    val familyClan: String,
    val religion: String,
    val duty: String,
    val funeralMethod: Boolean,
    val cause: Int,
    val papers: Boolean,
    val autopsyCheck: Boolean




)
