package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.DirectorService
import com.base.newPeaceSystemBuild.service.MemberService
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.ResultData
import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartRequest
import java.util.*
import javax.servlet.http.HttpServletRequest


@Controller
class UsrDirectorController(
    private val memberService: MemberService,
    private val directorService: DirectorService
) {
    @Autowired
    private lateinit var rq: Rq;

    // VIEW Mapping 함수 시작
    @RequestMapping("/usr/director/request")
    fun showRequest(): String {
        return "usr/director/request"
    }
    // VIEW Mapping 함수 끝

    @RequestMapping("/usr/director/doRequest", method = [RequestMethod.POST])
    @ResponseBody
    fun doRequest(
        multipartRequest: MultipartRequest
    ): String {
        val fileMap = multipartRequest.fileMap
        for (fileInputName in fileMap.keys) {
            val multipartFile = fileMap[fileInputName]
            val fileInputNameBits: List<String> = fileInputName.split("__")

            val fileSize = multipartFile!!.size.toInt()

            if (fileSize <= 0) {
                continue
            }

            val relId: Int = rq.getLoginedMember()!!.id
            val fileNo = fileInputNameBits[5].toInt()
            val originFileName = multipartFile.originalFilename!!
            val typeCode = fileInputNameBits[3]
            val type2Code = fileInputNameBits[4]
            val fileExtTypeCode: String = Ut.getFileExtTypeCodeFromFileName(multipartFile.originalFilename!!)
            val fileExtType2Code: String = Ut.getFileExtType2CodeFromFileName(multipartFile.originalFilename!!)
            val fileExt: String = Ut.getFileExtFromFileName(multipartFile.originalFilename!!).lowercase(Locale.getDefault())
            val fileDir: String = Ut.getNowYearMonthDateStr()

            directorService.putInForDirector(relId, originFileName, typeCode, type2Code, fileExtTypeCode, fileExtType2Code, fileExt, fileNo, fileSize, fileDir)
        }
        return rq.replaceJs("장례지도사 영업신청이 완료되었습니다.", "../home/main")
    }
}