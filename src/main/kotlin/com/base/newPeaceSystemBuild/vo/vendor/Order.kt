package com.base.newPeaceSystemBuild.vo.vendor

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

    val retailPriceMulBunchPrice: Int
        get() {
            return (extra__retailPrice ?: 0) * (extra__bunch ?: 0)
        }
    val retailPriceMulBunchPriceMulBunchCnt: Int
        get() {
            return (extra__retailPrice ?: 0) * (extra__bunch ?: 0) * (extra__bunchCnt ?: 0)
        }
}