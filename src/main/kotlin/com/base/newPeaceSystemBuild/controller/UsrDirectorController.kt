package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.DirectorService
import com.base.newPeaceSystemBuild.service.MemberService
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.ResultData
import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
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

    @RequestMapping("/usr/director/doRequest")
    @ResponseBody
    fun doRequest(
        @RequestParam director: MultipartFile,
        req: HttpServletRequest,
        multipartRequest: MultipartRequest
    ): ResultData? {
        val loginedMemberId = rq.getLoginedMember()!!.id

        val fileMap = multipartRequest.fileMap
        for (fileInputName in fileMap.keys) {
            val multipartFile = fileMap[fileInputName]
            val fileInputNameBits = fileInputName.split("__").toTypedArray()
            if (fileInputNameBits[0] == "file" == false) {
                continue
            }
            val fileSize = multipartFile!!.size.toInt()
            if (fileSize <= 0) {
                continue
            }
            val fileNo = fileInputNameBits[5].toInt()
            val originFileName = multipartFile.originalFilename
            val fileExtTypeCode: String = Ut.getFileExtTypeCodeFromFileName(multipartFile.originalFilename)
            val fileExtType2Code: String = Ut.getFileExtType2CodeFromFileName(multipartFile.originalFilename)
            val fileExt: String = Ut.getFileExtFromFileName(multipartFile.originalFilename).lowercase(Locale.getDefault())
            val fileDir: String = Ut.getNowYearMonthDateStr()
            directorService.putInForDirector(loginedMemberId, originFileName, fileExtTypeCode, fileExtType2Code, fileExt, fileNo, fileDir)
        }
        return addArticleRd
    }
}