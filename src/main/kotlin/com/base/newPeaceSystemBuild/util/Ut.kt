package com.base.newPeaceSystemBuild.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
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

//      정규표현식과 정규표현식을 적용할 값을 파라미터로 받음.
//      정규표현식에 해당할 경우, true를 return
        fun match(regex: String, toCompare: String): Boolean{
            val pattern: Pattern = Pattern.compile(regex)
            val matcher: Matcher = pattern.matcher(toCompare)

            return matcher.matches()
        }



        fun mapOf(vararg args: Any): Map<String, Any> {
            if (args.size % 2 != 0) {
                throw IllegalArgumentException("인자를 짝수개 입력해주세요.")
            }
            val size = args.size / 2
            val map: MutableMap<String, Any> = LinkedHashMap()
            for (i in 0 until size) {
                val keyIndex = i * 2
                val valueIndex = keyIndex + 1
                var key: String = try {
                    args[keyIndex] as String
                } catch (e: ClassCastException) {
                    throw IllegalArgumentException("키는 String으로 입력해야 합니다. " + e.message)
                }
                var value: Any = args[valueIndex]
                map[key] = value
            }
            return map
        }

        fun getFileExtTypeCodeFromFileName(fileName: String): String {
            val ext = getFileExtFromFileName(fileName).lowercase(Locale.getDefault())
            when (ext) {
                "jpeg", "jpg", "gif", "png" -> return "img"
                "mp4", "avi", "mov" -> return "video"
                "mp3" -> return "audio"
            }
            return "etc"
        }

        fun getFileExtType2CodeFromFileName(fileName: String): String {
            val ext = getFileExtFromFileName(fileName).lowercase(Locale.getDefault())
            when (ext) {
                "jpeg", "jpg" -> return "jpg"
                "gif" -> return ext
                "png" -> return ext
                "mp4" -> return ext
                "mov" -> return ext
                "avi" -> return ext
                "mp3" -> return ext
            }
            return "etc"
        }

        fun getFileExtFromFileName(fileName: String): String {
            val pos = fileName.lastIndexOf(".")
            return fileName.substring(pos + 1)
        }

        fun getNowYearMonthDateStr(): String {
            val format1 = SimpleDateFormat("yyyy_MM")
            return format1.format(System.currentTimeMillis())
        }
    }
}