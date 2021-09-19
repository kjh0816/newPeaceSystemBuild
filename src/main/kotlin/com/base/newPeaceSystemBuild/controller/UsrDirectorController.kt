package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.*
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.ResultData
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
        model: Model
    ): String {
        val funeral = clientService.getProgressingFuneralByDirectorMemberId(rq.getLoginedMember()!!.id)

        if(funeral == null){
            return "redirect:/usr/home/main"
        }

        //
        val client = clientService.getClientById(funeral.clientId)


        if(client == null){
            return "redirect:/usr/home/main"
        }
        // 헌화의 주문정보를 장례지도사 회원정보로 조회한 결과를 가져온다
        val flowerTributeOrder = vendorService.getFlowerTributeOrderByDirectorMemberId(rq.getLoginedMember()!!.id)

        // 뷰페이지에서 선택된 스탠다드의 가격을 표기하기 위해 불러온다
        val flower = vendorService.getFlowerById(funeral.flowerId)
        val flowerTribute = vendorService.getFlowerTributeById(funeral.flowerTributeId)

        // 뷰페이지에서 총액을 표기해주기 위한 변수들
        // 선택하지 않은상태에선 해당 변수(flower, portrait 등) 들이 null값을 가지고있다.
        // null 값을 허용하면서 null 일경우 해당 상품의 가격을 0으로 측정해서 넣어줌.
        var flowerPrice = 0
        var flowerTributePrice = 0

        if(flower != null){
            flowerPrice = flower.retailPrice.toInt()
        }
        if(flowerTribute != null){
            flowerTributePrice = (flowerTribute.retailPrice.toInt() * flowerTribute.bunch) * flowerTributeOrder.extra__bunchCnt!!.toInt()
        }


        model.addAttribute("client", client)
        model.addAttribute("funeral", funeral)
        model.addAttribute("flower", flower)
        model.addAttribute("flowerTribute", flowerTribute)
        model.addAttribute("flowerTributePrice", flowerTributePrice)
        model.addAttribute("flowerTributeOrder", flowerTributeOrder)
        model.addAttribute("sum", flowerPrice + flowerTributePrice)

        return "usr/director/progress"
    }

    @RequestMapping("/usr/director/selectFlower")
    fun showSelectFlower(model: Model): String {
        val funeral = clientService.getProgressingFuneralByDirectorMemberId(rq.getLoginedMember()!!.id)
        val flowers = vendorService.getFlowers()
        val flowerTributes = vendorService.getFlowerTributes()

        model.addAttribute("flowerTributes", flowerTributes)
        model.addAttribute("flowers", flowers)
        model.addAttribute("funeral", funeral)

        return "usr/director/selectFlower"
    }

    @RequestMapping("/usr/director/dispatch")
    fun showDispatch(
            model: Model,
            @RequestParam(defaultValue = "0") clientId: Int
    ): String {

        // 존재하지 않는 clientId를 URL로 접근하는 경우에 대한 예외처리
        val client = clientService.getClientById(clientId)

        if(client == null){
            return "usr/home/main"
        }

        model.addAttribute("client", client)

        return "usr/director/dispatch"
    }


    @RequestMapping("/usr/director/doDispatch", method = [RequestMethod.POST])
    @ResponseBody
    fun doDispatch(
            @RequestParam(defaultValue = "0") clientId: Int
    ): String {
        return Ut.getJsonStrFromObj(clientService.modifyClientIntoDirectorMemberIdByClientId(rq.getLoginedMember()!!.id, clientId))
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
        memberRoleService.insertMemberRole(introduce, rq.getLoginedMember()!!.id, 3, 0)

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
        @RequestParam(defaultValue = "0") flowerId: Int,
        @RequestParam(defaultValue = "0") flowerTributeId: Int,
        @RequestParam(defaultValue = "0") bunchCnt: Int,
        @RequestParam(defaultValue = "N") packing: Char
    ): String {
        return Ut.getJsonStrFromObj(vendorService.modifyFuneralIntoFlowerId(funeralId, flowerId, flowerTributeId, bunchCnt, packing))
    }

    @RequestMapping("/usr/director/moveProgress", method = [RequestMethod.POST])
    @ResponseBody
    fun doMoveProgressPage(
    ): String {
        val funeral = clientService.getProgressingFuneralByDirectorMemberId(rq.getLoginedMember()!!.id)

        if(funeral == null){
            return Ut.getJsonStrFromObj(clientService.moveProgressRd(0))
        }

        return Ut.getJsonStrFromObj(clientService.moveProgressRd(funeral.clientId))
    }




    // VIEW 기능 함수 끝
}