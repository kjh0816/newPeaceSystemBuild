package com.base.newPeaceSystemBuild.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class Util {
    //    전역 변수, 함수를 설정할 수 있는 companion object
    companion object {
        //        <reified T>를 통해 어떤 데이터 타입인지 알 수 있다.
        inline fun <reified T> getObjFromJsonStr(jsonStr: String): T {
            val mapper = ObjectMapper().registerKotlinModule()

            return mapper.readValue<T>(jsonStr)
        }


        fun getJsonStrFromObj(obj: Any): String {
            val mapper = ObjectMapper().registerKotlinModule()

            return mapper.writeValueAsString(obj)
        }
    }
}