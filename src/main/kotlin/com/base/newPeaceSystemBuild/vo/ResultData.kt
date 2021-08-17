package com.base.newPeaceSystemBuild.vo

import com.base.newPeaceSystemBuild.util.Ut


class ResultData(
    private val resultCode: String,
    private val msg: String,
    private vararg val keyOrValue: Any
) {
    companion object {


        fun from(resultCode: String, msg: String, vararg keyOrValue: Any): ResultData {
//            println("ResultData에서 들어온 값:" + Ut.getJsonStrFromObj(keyOrValue))
            return ResultData(resultCode, msg, Ut.mapOf(keyOrValue))
        }


    }

    fun isSuccess(): Boolean {
        return resultCode.startsWith("S-")
    }

    fun isFail(): Boolean {
        return !isSuccess()
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