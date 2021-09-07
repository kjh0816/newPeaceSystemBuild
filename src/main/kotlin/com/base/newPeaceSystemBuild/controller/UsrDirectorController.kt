package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.*
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
    private val vendorService: VendorService,
    private val clientService: ClientService
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
        val funeral = clientService.getProgressingFuneral(rq.getLoginedMember()!!.id)

        if(funeral == null){
            return "redirect:/usr/home/main"
        }

        //
        val client = clientService.getClientById(clientId)

        if(client == null){
            return "redirect:/usr/home/main"
        }
        // 뷰페이지에서 선택된 스탠다드의 가격을 표기하기 위해 불러온다
        val flower = vendorService.getFlowerById(funeral.flowerId)

        model.addAttribute("client", client)
        model.addAttribute("funeral", funeral)
        model.addAttribute("flower", flower)

        return "usr/director/progress"
    }

    @RequestMapping("/usr/director/selectFlower")
    fun showSelectFlower(model: Model): String {
        val funeral = clientService.getProgressingFuneral(rq.getLoginedMember()!!.id)
        val flowers = vendorService.getFlowers()

        model.addAttribute("flowers", flowers)
        model.addAttribute("funeral", funeral)

        return "usr/director/selectFlower"
    }

    @RequestMapping("/usr/director/dispatch")
    fun showDispatch(model: Model, clientId: Int): String {
        val client = clientService.getClientById(clientId)

        model.addAttribute("client", client)

        return "usr/director/dispatch"
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

        // memberRole 테이블에 유저가 입력한 자기소개 및 회원번호, roleLevel(roleId) 을 3으로 데이터를 생성
        memberRoleService.insertMemberRole(introduce, rq.getLoginedMember()!!.id, 3)

        // roleLevel을 3(장례지도사) 으로 변경
        memberService.modifyMemberIntoRoleLevelByMemberId(rq.getLoginedMember()!!.id, 3)

        // requestStatus 칼럼을 true로 변경
        memberService.modifyMemberIntoRequestStatusByMemberId(rq.getLoginedMember()!!.id, true)

        // 장례지도사 신청되면 회원정보가 갱신됨으로 현재 로그인중인 사용자의 세션또한 갱신해준다
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
        @RequestParam(defaultValue = "0") clientId: Int
    ): String {
        return Ut.getJsonStrFromObj(clientService.getClientByIdRd(clientId))
    }

    @RequestMapping("/usr/director/doDispatch", method = [RequestMethod.POST])
    @ResponseBody
    fun doDispatch(
        @RequestParam(defaultValue = "") clientId: Int
    ): String {
        return Ut.getJsonStrFromObj(clientService.modifyClientIntoDirectorMemberIdByClientId(rq.getLoginedMember()!!.id, clientId))
    }
}