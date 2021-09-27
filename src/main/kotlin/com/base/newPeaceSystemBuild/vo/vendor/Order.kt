package com.base.newPeaceSystemBuild.vo.vendor

import java.text.DecimalFormat

data class Order (
    val id: Int,
    val regDate: String,
    var updateDate: String,
    var vendorMemberId: Int,
    val clientId: Int,
    val directorMemberId: Int,
    val roleCategoryId: Int,
    val standardId: Int,
    var orderStatus: Boolean,
    var completionStatus: Boolean,
    val detail: String
) {
    var extra__retailPrice: Int? = null
    // 헌화 주문정보 관련 데이터
    var extra__bunchCnt: Int? = null
    var extra__bunch: Int? = null
    var extra__packing: Boolean? = null
    // 상복(여) 주문정보 관련 데이터
    var extra__femaleClothCnt: Int? = null
    var extra__femaleClothColor: String? = null
    // 상복(남) 주문정보 관련 데이터
    var extra__maleClothCnt: Int? = null

    private val formatter = DecimalFormat("###,###")

    val retailPriceMulBunchPrice: String
        get() {
            return formatter.format((extra__retailPrice ?: 0) * (extra__bunch ?: 0))
        }
    val retailPriceMulBunchPriceMulBunchCnt: String
        get() {
            return formatter.format((extra__retailPrice ?: 0) * (extra__bunch ?: 0) * (extra__bunchCnt ?: 0))
        }
}