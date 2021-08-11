package com.base.newPeaceSystemBuild.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.net.URLEncoder
import java.util.regex.Matcher
import java.util.regex.Pattern

class Ut {
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

        // URI 인코딩 함수
        fun getUriEncoded(uri: String): String {
            return try {
                URLEncoder.encode(uri, "UTF-8")
            } catch (e: Exception) {
                uri
            }
        }

        // 이메일이 표준형식에 맞는지 체크해주는 함수
        fun isValidEmail(email: String?): Boolean {
            var err = false
            val regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"
            val p: Pattern = Pattern.compile(regex)
            val m: Matcher = p.matcher(email)
            if (m.matches()) {
                err = true
            }
            return err
        }
    }
}