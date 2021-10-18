package com.base.newPeaceSystemBuild.vo.standard

import java.text.DecimalFormat


data class CoffinTransporter(
    val id: Int,
    val regDate: String,
    var updateDate: String,
    val funeralId: Int,
    val memberId: Int,
    val price: String,
    val departureAddress: String,
    var destinationAddress: String,
    val completionStatus: Boolean
)
{
    private val formatter = DecimalFormat("###,###")

    val priceFormatted: String
        get() {
            return formatter.format(price.toInt())
        }
}