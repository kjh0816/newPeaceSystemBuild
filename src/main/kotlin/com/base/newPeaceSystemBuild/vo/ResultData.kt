package com.base.newPeaceSystemBuild.vo

import com.base.newPeaceSystemBuild.util.Ut


class ResultData(
    private val resultCode: String,
    private val msg: String,
    private vararg val map: Any
) {
    companion object {


        fun from(resultCode: String, msg: String, vararg map: Any): ResultData {
            return ResultData(resultCode, msg, Ut.mapOf(map))
        }


    }

    fun isSuccess(): Boolean {
        return resultCode.startsWith("S-")
    }

    fun isFail(): Boolean {
        return isSuccess() == false
    }

    fun getMsg(): String {
        return msg
    }

    fun getResultCode(): String {
        return resultCode
    }

    fun getMap(): Map<String, Any> {
        return Ut.mapOf()
    }


}