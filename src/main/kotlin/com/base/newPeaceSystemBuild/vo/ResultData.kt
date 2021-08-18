package com.base.newPeaceSystemBuild.vo

import com.base.newPeaceSystemBuild.util.Ut


class ResultData(
    private val resultCode: String,
    private val msg: String,
    private val body: Map<String, Any>
) {
    companion object {
        fun from(resultCode: String, msg: String, vararg keyOrValue: Any): ResultData {

            var arr = arrayListOf<Any>()
            for(keyValue in keyOrValue){
                arr.add(keyValue)
            }


            return ResultData(resultCode, msg, Ut.arrayToMap(arr))
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
        return body
    }


}