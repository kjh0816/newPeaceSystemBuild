package com.base.newPeaceSystemBuild.vo.standard

import java.text.DecimalFormat

data class FlowerTribute(
    val id: Int,
    val regDate: String,
    var updateDate: String,
    var retailPrice: String,
    var costPrice: String,
    val bunch: Int
)
{
    private val formatter = DecimalFormat("###,###")

    val retailPriceFormat: String
        get() {
            return formatter.format(retailPrice.toInt())
        }
}
