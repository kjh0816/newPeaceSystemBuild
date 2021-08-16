package com.base.newPeaceSystemBuild.vo

import com.base.newPeaceSystemBuild.util.Ut


class ResultData() {
    private var msg: String? = null

    private var resultCode: String? = null

    private var body: Map<String, Any>? = null

    val isSuccess: Boolean
        get() = resultCode!!.startsWith("S-")

    val isFail: Boolean
        get() = !isSuccess

    companion object {
        fun from(resultCode: String, msg: String, vararg bodyArgs: Any?): ResultData {
            val rd = ResultData()
            rd.resultCode = resultCode
            rd.msg = msg
            rd.body = Ut.mapOf(bodyArgs)
            return rd
        }
    }
}