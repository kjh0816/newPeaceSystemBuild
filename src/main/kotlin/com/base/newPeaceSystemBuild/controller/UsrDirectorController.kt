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
import java.text.DecimalFormat


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

        if (funeral == null) {
            return "redirect:/usr/home/main"
        }

        //
        val client = clientService.getClientById(funeral.clientId)

        if (client == null) {
            return "redirect:/usr/home/main"
        }
        // 상주의 정보
        val chief = clientService.getFamilyByClientId(funeral.clientId)

        // 장례식장 선택을 위해 최초 로딩 시, 시/도 정보를 넘겨준다.


        // 헌화의 주문정보를 장례지도사 회원정보로 조회한 결과를 가져온다
        val flowerTributeOrder = vendorService.getOrderByDirectorMemberIdAndCompletionStatusAndDetail(rq.getLoginedMember()!!.id, false, "flowerTribute")
        val femaleMourningClothOrder = vendorService.getOrderByDirectorMemberIdAndCompletionStatusAndDetail(rq.getLoginedMember()!!.id, false, "femaleMourningCloth")
        val maleMourningClothOrder = vendorService.getOrderByDirectorMemberIdAndCompletionStatusAndDetail(rq.getLoginedMember()!!.id, false, "maleMourningCloth")

        // 뷰페이지에서 선택된 스탠다드의 가격을 표기하기 위해 불러온다
        val flower = vendorService.getFlowerById(funeral.flowerId)
        val flowerTribute = vendorService.getFlowerTributeById(funeral.flowerTributeId)
        val femaleMourningCloth = vendorService.getFemaleMourningClothById(funeral.femaleMourningClothId)
        val maleMourningCloth = vendorService.getMaleMourningClothById(funeral.maleMourningClothId)

        // 뷰페이지에서 총액을 표기해주기 위한 변수들
        // 선택하지 않은상태에선 해당 변수(flower, portrait 등) 들이 null값을 가지고있다.
        // null 값을 허용하면서 null 일경우 해당 상품의 가격을 0으로 측정해서 넣어줌.
        var flowerPrice = 0
        var flowerTributePrice = 0
        var femaleMourningClothPrice = 0
        var maleMourningClothPrice = 0

        val formatter = DecimalFormat("###,###")
        // 합계
        var sum = 0

        if (flower != null) {
            flowerPrice = flower.retailPrice.toInt()
            sum += flowerPrice
        }
        if (flowerTribute != null) {
            flowerTributePrice =
                    (flowerTribute.retailPrice.toInt() * flowerTribute.bunch) * flowerTributeOrder!!.extra__bunchCnt!!
            sum += flowerTributePrice
        }
        if (femaleMourningCloth != null) {
            femaleMourningClothPrice = femaleMourningCloth.retailPrice.toInt() * femaleMourningClothOrder!!.extra__femaleClothCnt!!
            sum += femaleMourningClothPrice
        }
        if (maleMourningCloth != null) {
            maleMourningClothPrice = maleMourningCloth.retailPrice.toInt() * maleMourningClothOrder!!.extra__maleClothCnt!!
            sum += maleMourningClothPrice
        }

        val flowerTributePriceFormat = formatter.format(flowerTributePrice)
        val femaleMourningClothPriceFormat = formatter.format(femaleMourningClothPrice)
        val maleMourningClothPriceFormat = formatter.format(maleMourningClothPrice)
        val sumFormat = formatter.format(sum)


        model.addAttribute("client", client)
        model.addAttribute("funeral", funeral)
        model.addAttribute("chief", chief)
//      선택된 스탠다드 종류
        model.addAttribute("flower", flower)
        model.addAttribute("flowerTribute", flowerTribute)
        model.addAttribute("femaleMourningCloth", femaleMourningCloth)
        model.addAttribute("maleMourningCloth", maleMourningCloth)
//      단품이 아닌 세트 혹은 다수의 상품을 선택해야하는것들, 선택한 갯수랑 개당가격을 계산한가격
        model.addAttribute("flowerTributePriceFormat", flowerTributePriceFormat)
        model.addAttribute("femaleMourningClothPriceFormat", femaleMourningClothPriceFormat)
        model.addAttribute("maleMourningClothPriceFormat", maleMourningClothPriceFormat)
//      상품별 Order 상세정보
        model.addAttribute("flowerTributeOrder", flowerTributeOrder)
        model.addAttribute("femaleMourningClothOrder", femaleMourningClothOrder)
        model.addAttribute("maleMourningClothOrder", maleMourningClothOrder)
//      합계
        model.addAttribute("sumFormat", sumFormat)

        return "usr/director/progress"
    }

    @RequestMapping("/usr/director/modifyFuneral")
    fun showModifyFuneral(
            model: Model,
            @RequestParam(defaultValue = "0") clientId: Int
    ): String {


        // 파라미터가 자동으로 입력되는 정상적인 접근이 아닌 경우에 대한 예외처리
        if (clientId == 0) {
            return "usr/home/main"
        }

        val funeral = clientService.getFuneralByClientId(clientId)
        // 잘못된 clientId 파라미터로 접근하는 경우에 대한 예외처리
        if (funeral == null) {
            return "usr/home/main"
        }

        val client = clientService.getClientById(clientId)

        // 해당 페이지에 접근한 장례지도사가 조회한 장례를 진행하는 장례지도사가 아닌 경우에 대한 예외처리
        if (rq.getLoginedMember()!!.id != funeral.directorMemberId || client == null) {
            return "usr/home/main"
        }



        @RequestMapping("/usr/member/departmentDetail", method = [RequestMethod.POST])
        @ResponseBody
        fun departmentDetail(
                @RequestParam(defaultValue = "") department: String
        ): String {

            return Ut.getJsonStrFromObj(memberRoleService.getFuneralHallsByDepartment(department.trim()))

        }

        // 상주 정보 불러옴
        val chief = clientService.getFamilyByClientId(clientId)

        val departments = memberService.getDepartments()

        model.addAttribute("client", client)
        model.addAttribute("funeral", funeral)
        model.addAttribute("chief", chief)
        model.addAttribute("departments", departments)

        return "usr/director/modifyFuneral"
    }

    @RequestMapping("/usr/director/selectFlower")
    fun showSelectFlower(model: Model): String {
        val funeral = clientService.getProgressingFuneralByDirectorMemberId(rq.getLoginedMember()!!.id)
        val flowers = vendorService.getFlowers()
        val flowerTributes = vendorService.getFlowerTributes()

        if (funeral != null) {
            val flowerOrder = vendorService.getOrderByClientIdAndDirectorMemberIdAndCompletionStatusAndDetail(
                    funeral.clientId,
                    rq.getLoginedMember()!!.id,
                    false,
                    "flower"
            )
            val flowerTributeOrder = vendorService.getOrderByClientIdAndDirectorMemberIdAndCompletionStatusAndDetail(
                    funeral.clientId,
                    rq.getLoginedMember()!!.id,
                    false,
                    "flowerTribute"
            )

            model.addAttribute("flowerOrder", flowerOrder)
            model.addAttribute("flowerTributeOrder", flowerTributeOrder)
        }


        model.addAttribute("flowerTributes", flowerTributes)
        model.addAttribute("flowers", flowers)
        model.addAttribute("funeral", funeral)

        return "usr/director/selectFlower"
    }

    @RequestMapping("/usr/director/selectMourningCloth")
    fun showSelectMourningCloth(model: Model): String {
        val funeral = clientService.getProgressingFuneralByDirectorMemberId(rq.getLoginedMember()!!.id)
        val femaleMourningCloths = vendorService.getFemaleMourningCloths()
        val maleMourningCloths = vendorService.getMaleMourningCloths()

        if (funeral != null) {
            val femaleMourningClothOrder = vendorService.getOrderByClientIdAndDirectorMemberIdAndCompletionStatusAndDetail(
                    funeral.clientId,
                    rq.getLoginedMember()!!.id,
                    false,
                    "femaleMourningCloth"
            )
            val maleMourningClothOrder = vendorService.getOrderByClientIdAndDirectorMemberIdAndCompletionStatusAndDetail(
                    funeral.clientId,
                    rq.getLoginedMember()!!.id,
                    false,
                    "maleMourningCloth"
            )

            model.addAttribute("femaleMourningClothOrder", femaleMourningClothOrder)
            model.addAttribute("maleMourningClothOrder", maleMourningClothOrder)
        }


        model.addAttribute("maleMourningCloths", maleMourningCloths)
        model.addAttribute("femaleMourningCloths", femaleMourningCloths)
        model.addAttribute("funeral", funeral)

        return "usr/director/selectMourningCloth"
    }

    @RequestMapping("/usr/director/dispatch")
    fun showDispatch(
            model: Model,
            @RequestParam(defaultValue = "0") clientId: Int
    ): String {

        // 존재하지 않는 clientId를 URL로 접근하는 경우에 대한 예외처리
        val client = clientService.getClientById(clientId)

        if (client == null) {
            return "usr/home/main"
        }

        val chief = clientService.getFamilyByClientId(clientId)

        model.addAttribute("chief", chief)
        model.addAttribute("client", client)


        return "usr/director/dispatch"
    }


    @RequestMapping("/usr/director/doDispatch", method = [RequestMethod.POST])
    @ResponseBody
    fun doDispatch(
            @RequestParam(defaultValue = "0") clientId: Int
    ): String {
        return Ut.getJsonStrFromObj(
                clientService.modifyClientIntoDirectorMemberIdByClientId(
                        rq.getLoginedMember()!!.id,
                        clientId
                )
        )
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

    @RequestMapping("/usr/director/doSelectMourningCloth", method = [RequestMethod.POST])
    @ResponseBody
    fun doSelectMourningCloth(
            funeralId: Int,
            @RequestParam(defaultValue = "0") femaleMourningClothId: Int,
            @RequestParam(defaultValue = "0") femaleClothCnt: Int,
            @RequestParam(defaultValue = "0") maleMourningClothId: Int,
            @RequestParam(defaultValue = "0") maleClothCnt: Int,
    ): String {
        var femaleClothColor = ""

        if (femaleMourningClothId == 1) {
            femaleClothColor = "흑"
        } else if (femaleMourningClothId == 2) {
            femaleClothColor = "백"
        }

        return Ut.getJsonStrFromObj(
                vendorService.modifyFuneralIntoFemaleMourningClothId(
                        funeralId,
                        femaleMourningClothId,
                        femaleClothCnt,
                        femaleClothColor,
                        maleMourningClothId,
                        maleClothCnt
                )
        )
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
        return Ut.getJsonStrFromObj(
                vendorService.modifyFuneralIntoFlowerId(
                        funeralId,
                        flowerId,
                        flowerTributeId,
                        bunchCnt,
                        packing
                )
        )
    }

    @RequestMapping("/usr/director/moveProgress", method = [RequestMethod.POST])
    @ResponseBody
    fun doMoveProgressPage(
    ): String {
        val funeral = clientService.getProgressingFuneralByDirectorMemberId(rq.getLoginedMember()!!.id)

        if (funeral == null) {
            return Ut.getJsonStrFromObj(clientService.moveProgressRd(0))
        }

        return Ut.getJsonStrFromObj(clientService.moveProgressRd(funeral.clientId))
    }


    // VIEW 기능 함수 끝
}