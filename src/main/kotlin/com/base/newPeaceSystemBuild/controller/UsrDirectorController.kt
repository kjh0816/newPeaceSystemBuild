package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.GenFileService
import com.base.newPeaceSystemBuild.service.MemberRoleService
import com.base.newPeaceSystemBuild.service.MemberService
import com.base.newPeaceSystemBuild.service.VendorService
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartRequest


@Controller
class UsrDirectorController(
    private val memberService: MemberService,
    private val genFileService: GenFileService,
    private val memberRoleService: MemberRoleService,
    private val vendorService: VendorService
) {
    @Autowired
    private lateinit var rq: Rq;

    // VIEW Mapping 함수 시작
    @RequestMapping("/usr/director/request")
    fun showRequest(): String {
        return "usr/director/request"
    }

    @RequestMapping("/usr/director/modify")
    fun showModify(): String {
        return "usr/director/modify"
    }

    @RequestMapping("/usr/director/progress")
    fun showProgress(
        model: Model,
        clientId: Int
    ): String {
        val funeral = memberService.getProgressingFuneral(rq.getLoginedMember()!!.id)

        if(funeral == null){
            return "redirect:/usr/home/main"
        }

        val client = memberService.getClientById(clientId)

        if(client == null){
            return "redirect:/usr/home/main"
        }
        val flower = vendorService.getFlowerById(funeral.flowerId)

        model.addAttribute("client", client)
        model.addAttribute("funeral", funeral)
        model.addAttribute("flower", flower)

        return "usr/director/progress"
    }

    @RequestMapping("/usr/director/selectFlower")
    fun showSelectFlower(model: Model): String {
        val funeral = memberService.getProgressingFuneral(rq.getLoginedMember()!!.id)
        val flowers = vendorService.getFlowers()

        model.addAttribute("flowers", flowers)
        model.addAttribute("funeral", funeral)

        return "usr/director/selectFlower"
    }
    // VIEW Mapping 함수 끝

    // VIEW 기능 함수 시작
    @RequestMapping("/usr/director/doRequest", method = [RequestMethod.POST])
    @ResponseBody
    fun doRequest(
        introduce: String,
        multipartRequest: MultipartRequest
    ): String {
        // Request 페이지에서 넘어온 파라미터를 DB에 추가하는 과정
        // 장례지도사 승인 신청 시, roleLevel(=roleId)는 3(장례지도사)가 되고, authenticationStatus에 따라서 이후 구분된다.

        val fileMap = multipartRequest.fileMap
        for (fileInputName in fileMap.keys) {
            val multipartFile = fileMap[fileInputName]

            if (multipartFile != null) {
                // 파일을 저장
                genFileService.save(multipartFile, rq.getLoginedMember()!!.id)
            }
        }

        //  장례지도사의 roleLevel(roleId)는 3
        //  승인 요청 시, roleLevel은 장례지도사가 되고, authenticationStatus로 구분된다.

        memberRoleService.insertDataIntoMemberRole(introduce, rq.getLoginedMember()!!.id, 3)

        memberService.updateRoleLevel(rq.getLoginedMember()!!.id, 3)

        // 장례지도사 신청은 회원데이터 수정이기 때문에 세션데이터를 수정된값으로 다시 넣어준다.

        rq.login(memberService.getMemberById(rq.getLoginedMember()!!.id)!!)

        return rq.replaceJs("장례지도사 영업신청이 완료되었습니다.", "../home/main")
    }

    @RequestMapping("/usr/director/doSelectFlower", method = [RequestMethod.POST])
    @ResponseBody
    fun doSelectFlower(
        funeralId: Int,
        flowerId: Int
    ): String {
        return Ut.getJsonStrFromObj(vendorService.modifyFuneralIntoFlowerId(funeralId, flowerId))
    }
    // VIEW 기능 함수 끝

    @RequestMapping("/usr/director/moveProgress", method = [RequestMethod.POST])
    @ResponseBody
    fun doMoveProgressPage(
        @RequestParam(defaultValue = "") clientId: Int
    ): String {
//      Ajax 요청을 ResultData 형식으로 응답한다.(Json 형식이므로, 값을 Ajax(JS)로 다룰 수 있다.)
        return Ut.getJsonStrFromObj(memberService.getClientByIdRd(clientId))

    }
}