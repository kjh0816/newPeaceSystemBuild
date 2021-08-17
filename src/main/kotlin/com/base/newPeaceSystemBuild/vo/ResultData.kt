package com.base.newPeaceSystemBuild.vo


class ResultData<T>(
    private val resultCode: String,
    private val msg: String,
    private val data1Name: String,
    private val data1: T
) {
    companion object {
        fun from(resultCode: String, msg: String): ResultData<String> {
            return ResultData(resultCode, msg, "", "")
        }

        fun <T> from(resultCode: String, msg: String, data1Name: String, data1: T): ResultData<T> {
            return ResultData(resultCode, msg, data1Name, data1)
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

    fun getData(): T {
        return data1
    }
}