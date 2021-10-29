package com.base.newPeaceSystemBuild.util


import com.base.newPeaceSystemBuild.vo.Aligo__send__ResponseBody
import com.base.newPeaceSystemBuild.vo.member.Member
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.HttpClients
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URLEncoder
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class Ut(

) {


    //    전역 변수, 함수를 설정할 수 있는 companion object
    companion object {

        private lateinit var aligoUserId: String
        private lateinit var aligoApiKey: String


        fun initAligo(aligoUserId: String, aligoApiKey: String) {
            Ut.aligoUserId = aligoUserId
            Ut.aligoApiKey = aligoApiKey
        }

        fun sendSms(from: String, to: String, msg: String, isTest: Boolean): Aligo__send__ResponseBody {


            try {

                val encodingType = "utf-8"
                val boundary = "____boundary____"


/*
 "result_code":결과 코드
   "message":결과 문구
   "msg_id":메세지ID
   "error_cnt":에러갯수
   "success_cnt":성공갯수
*/

                val sms_url = "https://apis.aligo.in/send/" // 전송요청 URL

                val sms: MutableMap<String, String> = LinkedHashMap<String, String>()

                /**************** 인증정보 (시작) ******************/

                sms["user_id"] = aligoUserId
                sms["key"] = aligoApiKey

                /******************** 인증정보 (끝) ********************/



                /******************** 전송정보 (이하) ********************/

                sms["msg"] = msg // 메세지 내용

                sms["receiver"] = to // 수신번호

                // sms.put("destination", "01111111111|담당자,01111111112|홍길동"); // 수신인 %고객명% 치환
                // sms.put("destination", "01111111111|담당자,01111111112|홍길동"); // 수신인 %고객명% 치환
                sms["sender"] = from // 발신번호

                // sms.put("rdate", ""); // 예약일자 - 20161004 : 2016-10-04일기준
                // sms.put("rtime", ""); // 예약시간 - 1930 : 오후 7시30분
                // sms.put("rdate", ""); // 예약일자 - 20161004 : 2016-10-04일기준
                // sms.put("rtime", ""); // 예약시간 - 1930 : 오후 7시30분
                sms["testmode_yn"] = if (isTest) "Y" else "N" // Y 인경우 실제문자 전송X , 자동취소(환불) 처리

                // sms.put("title", "제목입력"); // LMS, MMS 제목 (미입력시 본문중 44Byte 또는 엔터 구분자 첫라인)

                // sms.put("title", "제목입력"); // LMS, MMS 제목 (미입력시 본문중 44Byte 또는 엔터 구분자 첫라인)
                val image = ""
                // image = "/tmp/pic_57f358af08cf7_sms_.jpg"; // MMS 이미지 파일 위치




                val builder: MultipartEntityBuilder = MultipartEntityBuilder.create()



                builder.setBoundary(boundary)
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                builder.setCharset(Charset.forName(encodingType))

                // i in sms 와 i in sms.iterator()의 차이가 없다.

                for (i in sms) {
                    val key = i.key

                    builder.addTextBody(key, sms[key], ContentType.create("Multipart/related", encodingType))
                }
//                사진 파일을 첨부하기 위한 코드 (시작) Multipart/related -> multipart/form-data 바꿔줘야함.
//                val imageFile = File(image)
//                if (image != null && image.length > 0 && imageFile.exists()) {
//
//                    builder.addPart(
//                        "image", FileBody(
//                            imageFile, ContentType.create("application/octet-stream"),
//                            URLEncoder.encode(imageFile.getName(), encodingType)
//                        )
//                    )
//                }
//                사진 파일을 첨부하기 위한 코드 (끝)



                val entity: HttpEntity = builder.build()

                val client: HttpClient = HttpClients.createDefault()
                val post = HttpPost(sms_url)
                post.entity = entity

                val res: HttpResponse = client.execute(post)



                var result = ""
                if (res != null) {
                    val input = BufferedReader(
                        InputStreamReader(res.entity.content, encodingType)
                    )


                    val buffer: String? = input.readLine()
                    if (buffer != null) {
                        result += buffer


                    }
                    input.close()
                }







                return getObjFromJsonStr(result)

            } catch (e: Exception) {

                println("Ut.sendSms함수가 Exception으로 빠짐")
                println("Ut.sendSms함수가 Exception으로 빠짐")
                println("Ut.sendSms함수가 Exception으로 빠짐")
                e.printStackTrace()
                val rb = Aligo__send__ResponseBody("Exception", e.localizedMessage, "-1", 0, 1, "msg")

                return rb
            }
        }


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
        fun match(regex: String, toCompare: String): Boolean {
            val pattern: Pattern = Pattern.compile(regex)
            val matcher: Matcher = pattern.matcher(toCompare)

            return matcher.matches()
        }

        fun arrayToMap(array: ArrayList<Any>): Map<String, Any> {

            if (array.size % 2 != 0) {
                throw IllegalArgumentException("인자를 짝수개 입력해주세요.");
            }

            val size = array.size / 2
            val map: MutableMap<String, Any> = LinkedHashMap()
            for (i in 0 until size) {
                val keyIndex = i * 2
                val valueIndex = keyIndex + 1

                val key: String = try {
                    array[keyIndex] as String
                } catch (e: ClassCastException) {
                    throw IllegalArgumentException("키는 String으로 입력해야 합니다. " + e.message)
                }

                val value: Any = array[valueIndex]
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

        fun getCellphoneNosFromMembers(members: List<Member>): String{

            val sb = StringBuilder()

                for(i in members.indices){

                    if(i == 0){
                        sb.append(members[i].cellphoneNo)
                    }else{
                        sb.append("," + members[i].cellphoneNo)
                    }
                }


            return sb.toString()

        }

        fun getTempPassword(length: Int): String {
            var index = 0
            val charArr = charArrayOf(
                    '0',
                    '1',
                    '2',
                    '3',
                    '4',
                    '5',
                    '6',
                    '7',
                    '8',
                    '9',
                    'a',
                    'b',
                    'c',
                    'd',
                    'e',
                    'f',
                    'g',
                    'h',
                    'i',
                    'j',
                    'k',
                    'l',
                    'm',
                    'n',
                    'o',
                    'p',
                    'q',
                    'r',
                    's',
                    't',
                    'u',
                    'v',
                    'w',
                    'x',
                    'y',
                    'z'
            )
            val sb = StringBuffer()
            for (i in 0 until length) {
                index = (charArr.size * Math.random()).toInt()
                sb.append(charArr[index])
            }
            return sb.toString()
        }

        fun getCellphoneNoFormatted(cellphoneNo: String): String{

            val sb = StringBuilder()

            sb.append(cellphoneNo.trim().substring(0, 3))
            sb.append("-")
            sb.append(cellphoneNo.substring(3, 7))
            sb.append("-")
            sb.append(cellphoneNo.substring(7, cellphoneNo.length))

            return sb.toString()
        }

        fun getDateStrLater(seconds: Long): String {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return format.format(System.currentTimeMillis() + seconds * 1000)
        }


    }
}
