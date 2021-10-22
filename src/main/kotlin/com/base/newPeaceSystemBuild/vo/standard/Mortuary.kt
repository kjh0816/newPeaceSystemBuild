package com.base.newPeaceSystemBuild.vo.standard

import java.text.DecimalFormat


data class Mortuary(
    val id: Int,
    val regDate: String,
    var updateDate: String,
    val name: String,
    var retailPrice: String,
    var costPrice: String
)
{
    private val formatter = DecimalFormat("###,###")

    val retailPriceFormat: String
        get() {
            return formatter.format(retailPrice.toInt())
        }
}