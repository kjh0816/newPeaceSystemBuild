package com.base.newPeaceSystemBuild.vo.standard

import java.text.DecimalFormat

data class Coffin(
        val id: Int,
        val name: String,
        val size: String,
        val chi: String,
        val costPrice: String,
        val retailPrice: String
){
    private val formatter = DecimalFormat("###,###")

    val retailPriceFormat: String
        get() {
            return formatter.format(retailPrice.toInt())
        }
}
