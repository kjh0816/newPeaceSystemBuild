package com.base.newPeaceSystemBuild.vo.vendor

import java.text.DecimalFormat

data class Order (
    val id: Int,
    val regDate: String,
    var updateDate: String,
    var vendorMemberId: Int,
    val funeralId: Int,
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
    var extra__femaleClothBlackCnt: Int? = null
    var extra__femaleClothWhiteCnt: Int? = null
    // 상복(남) 주문정보 관련 데이터
    var extra__maleClothCnt: Int? = null
    // 와이셔츠 주문정보 관련 데이터
    var extra__shirtCnt: Int? = null
    // 넥타이 주문정보 관련 데이터
    var extra__necktieCnt: Int? = null
    // 운구차량 주문정보 관련 데이터
    var extra__deceasedHomeAddress: String? = null
    // 빈소용품 주문정보 관련 데이터
    var extra__incenseCnt: Int? = null
    var extra__candleCnt: Int? = null
    var extra__ancestralTabletCnt: Int? = null
    var extra__condolenceMoneyBookCnt: Int? = null
    var extra__condolenceBookCnt: Int? = null
    var extra__pictureRibbonCnt: Int? = null

    private val formatter = DecimalFormat("###,###")

    // 헌화 한 세트당 가격
    val retailPriceFormat: String
        get() {
            return formatter.format(extra__retailPrice)
        }

    // 헌화 한 세트당 가격
    val retailPriceMulBunchPrice: String
        get() {
            if(extra__packing == true){
                return formatter.format((extra__retailPrice ?: 0) * (extra__bunch ?: 0) * 3)
            }
            return formatter.format((extra__retailPrice ?: 0) * (extra__bunch ?: 0))
        }
    // 헌화 총 가격
    val retailPriceMulBunchPriceMulBunchCnt: String
        get() {
            if(extra__packing == true){
                return formatter.format((extra__retailPrice ?: 0) * (extra__bunch ?: 0) * (extra__bunchCnt ?: 0) * 3)
            }
            return formatter.format((extra__retailPrice ?: 0) * (extra__bunch ?: 0) * (extra__bunchCnt ?: 0))
        }
}