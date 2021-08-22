package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.GenFileService
import com.base.newPeaceSystemBuild.service.MemberRoleService
import com.base.newPeaceSystemBuild.service.MemberService
import com.base.newPeaceSystemBuild.service.VendorService
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.standard.Flower
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartRequest


@Controller
class UsrVendorController(
    private val memberService: MemberService,
    private val genFileService: GenFileService,
    private val memberRoleService: MemberRoleService,
    private val vendorService: VendorService
) {
    @Autowired
    private lateinit var rq: Rq;


    @RequestMapping("/usr/vendor/explain")
    fun showRequest(): String {


        return "usr/vendor/explain"
    }

    @RequestMapping("/usr/vendor/request")
    fun showRequest(model: Model): String{

        val flowers: List<Flower> = vendorService.getFlowers()


        model.addAttribute("flowers", flowers)

        return "usr/vendor/request"
    }

    @RequestMapping("/usr/vendor/doRequest", method = [RequestMethod.POST])
    @ResponseBody
    fun doRequest(
        multipartRequest: MultipartRequest
    ): String {


        val fileMap = multipartRequest.fileMap
        for (fileInputName in fileMap.keys) {
            val multipartFile = fileMap[fileInputName]

            if (multipartFile != null) {
                // 파일을 저장
                genFileService.save(multipartFile, rq.getLoginedMember()!!.id)
            }

//      제단꽃 공급업자는 자기소개(introduce)가 필요없어서 공백을 넣는다.
//      물품 공급업자의 roleLevel(roleId)는 4
            val introduce = ""
            memberRoleService.insertDataIntoMemberRole(introduce, rq.getLoginedMember()!!.id, 4)
            memberService.updateRoleLevel(rq.getLoginedMember()!!.id, 4)
        }

        // 물품 공급업자 등록 승인 요청 시, 로그인 세션 데이터를 바꿔줘야한다.
        rq.login(memberService.getMemberById(rq.getLoginedMember()!!.id)!!)

        return rq.replaceJs("제단꽃 공급업자 등록 신청이 완료되었습니다.", "../home/main")
    }
}