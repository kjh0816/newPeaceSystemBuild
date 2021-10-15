package com.base.newPeaceSystemBuild.vo.standard

import java.text.DecimalFormat


data class CoffinTransporter(
    val id: Int,
    val regDate: String,
    var updateDate: String,
    val funeralId: String,
    val memberId: String,
    val price: String,
    val departureAddress: String,
    var destinationAddress: String,
    val completionStatus: Int
)
{
    private val formatter = DecimalFormat("###,###")

    val priceFormatted: String
        get() {
            return formatter.format(price.toInt())
        }
}