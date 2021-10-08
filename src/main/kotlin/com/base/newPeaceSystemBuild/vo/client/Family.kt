package com.base.newPeaceSystemBuild.vo.client

data class Family(
        val id: Int,
        val regDate: String,
        val updateDate: String,
        val clientId: Int,
        val chiefStatus: Boolean,
        val name: String,
        val relation: String,
        val cellphoneNo: String,
        // 상주만 받는 데이터
        val address: String,
        val bank: String,
        val accountNum: String,
        val accountOwner: String,
        val familyLastId: Int
)