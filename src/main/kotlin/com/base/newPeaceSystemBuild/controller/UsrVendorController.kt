package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.*
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.standard.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartRequest


@Controller
class UsrVendorController(
    private val memberService: MemberService,
    private val genFileService: GenFileService,
    private val memberRoleService: MemberRoleService,
    private val vendorService: VendorService,
    private val clientService: ClientService
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
        val flowerTributes: List<FlowerTribute> = vendorService.getFlowerTributes()


        model.addAttribute("flowers", flowers)
        model.addAttribute("flowerTributes", flowerTributes)

        return "usr/vendor/request"
    }

    @RequestMapping("/usr/vendor/mourningClothRequest")
    fun showMourningClothRequest(model: Model): String{

        val femaleMourningClothBlacks: List<MourningCloth> = vendorService.getFemaleMourningClothBlacks()
        val femaleMourningClothWhites: List<MourningCloth> = vendorService.getFemaleMourningClothWhites()
        val maleMourningCloths: List<MourningCloth> = vendorService.getMaleMourningCloths()
        val shirts: List<MourningCloth> = vendorService.getShirts()
        val neckties: List<MourningCloth> = vendorService.getNeckties()


        model.addAttribute("femaleMourningClothBlacks", femaleMourningClothBlacks)
        model.addAttribute("femaleMourningClothWhites", femaleMourningClothWhites)
        model.addAttribute("maleMourningCloths", maleMourningCloths)
        model.addAttribute("shirts", shirts)
        model.addAttribute("neckties", neckties)

        return "usr/vendor/mourningClothRequest"
    }

    @RequestMapping("/usr/vendor/coffinTransporterRequest")
    fun showCoffinTransporterRequest(model: Model): String{
        val coffinTransporters: List<CoffinTransporter> = vendorService.getCoffinTransporters()

        model.addAttribute("coffinTransporters", coffinTransporters)

        return "usr/vendor/coffinTransporterRequest"
    }

    @RequestMapping("/usr/vendor/coffinTransporterDispatch")
    fun showCoffinTransporterDispatch(
            model: Model,
            @RequestParam(defaultValue = "0") clientId: Int
    ): String{

        // 현재 접속한 회원이 운구차 운전자가 아닌 경우는 잘못된 접근
        if(rq.getLoginedMember()!!.roleLevel != 4 || rq.getLoginedMember()!!.extra__roleCategoryId != 3){
            return "usr/home/main"
        }


        // 존재하지 않는 clientId를 URL로 접근하는 경우에 대한 예외처리
        val client = clientService.getClientById(clientId)
        val funeral = clientService.getFuneralByClientId(clientId)

        if (client == null || funeral == null) {
            return "usr/home/main"
        }

        val coffinTransporter = vendorService.getCoffinTransporterByFuneralId(funeral.id)
        if(coffinTransporter == null){
            return "usr/home/main"
        }

        model.addAttribute("coffinTransporter", coffinTransporter)

        return "usr/vendor/coffinTransporterDispatch"
    }

    @RequestMapping("/usr/vendor/doCoffinTransporterDispatch", method = [RequestMethod.POST])
    @ResponseBody
    fun doCoffinTransporterDispatch(
            @RequestParam(defaultValue = "0") clientId: Int
    ): String{


        return Ut.getJsonStrFromObj(vendorService.doCoffinTransporterDispatch(clientId))

    }

    @RequestMapping("/usr/vendor/coffinTransporterProgress")
    fun showCoffinTransporterProgress(
            model: Model,
            @RequestParam(defaultValue = "0") clientId: Int
    ): String{

        // 현재 접속한 회원이 운구차 운전자가 아닌 경우는 잘못된 접근
        if(rq.getLoginedMember()!!.roleLevel != 4 || rq.getLoginedMember()!!.extra__roleCategoryId != 3){
            return "usr/home/main"
        }


        // 존재하지 않는 clientId를 URL로 접근하는 경우에 대한 예외처리
        val client = clientService.getClientById(clientId)
        val funeral = clientService.getFuneralByClientId(clientId)

        if (client == null || funeral == null) {
            return "usr/home/main"
        }


        val coffinTransporter = vendorService.getCoffinTransporterByFuneralId(funeral.id)
        if(coffinTransporter == null){
            return "usr/home/main"
        }


        val directorMember = memberService.getMemberById(funeral.directorMemberId)

        val directorCellphoneNo = Ut.getCellphoneNoFormatted(directorMember!!.cellphoneNo)

        val chief = clientService.getFamilyByClientId(clientId)

        val chiefCellphoneNo = Ut.getCellphoneNoFormatted(chief.cellphoneNo)


        model.addAttribute("chief", chief)
        model.addAttribute("coffinTransporter", coffinTransporter)
        model.addAttribute("directorMember", directorMember)
        model.addAttribute("directorCellphoneNo", directorCellphoneNo)
        model.addAttribute("chiefCellphoneNo", chiefCellphoneNo)



        return "usr/vendor/coffinTransporterProgress"
    }

    @RequestMapping("/usr/vendor/doCoffinTransporterProgress")
    @ResponseBody
    fun doCoffinTransporterProgress(
            @RequestParam(defaultValue = "0") clientId: Int
    ): String{
        return Ut.getJsonStrFromObj(vendorService.doCoffinTransporterProgress(clientId))
    }

    @RequestMapping("/usr/vendor/shroudRequest")
    fun showShroudRequest(model: Model): String{
        val shrouds: List<Shroud> = vendorService.getShrouds()

        model.addAttribute("shrouds", shrouds)

        return "usr/vendor/shroudRequest"
    }

    @RequestMapping("/usr/vendor/dispatch")
    fun showDispatch(
            model: Model,
            clientId: Int,
            funeralId: Int,
            directorMemberId: Int
    ): String {
        val client = clientService.getClientById(clientId)
        // clientId로 funeral을 조회하는 SQL인데, funeralId로 조회하는 것이 문제가 있음.
        val funeral = clientService.getFuneralByClientId(funeralId)

        if(funeral == null){
            return "redirect:/usr/home/main"
        }

        val flower = vendorService.getFlowerById(funeral.flowerId)
        val flowerTribute = vendorService.getFlowerTributeById(funeral.flowerTributeId)
        val flowerTributeOrder = vendorService.getOrderByCompletionStatusAndDetailAndFuneralId(false, "flowerTribute", funeralId)

        val chief = clientService.getFamilyByClientId(clientId)

        model.addAttribute("chief", chief)
        model.addAttribute("client", client)
        model.addAttribute("funeral", funeral)
        // 스탠다드 정보
        model.addAttribute("flower", flower)
        model.addAttribute("flowerTribute", flowerTribute)
        // 오더 정보
        model.addAttribute("flowerTributeOrder", flowerTributeOrder)

        return "usr/vendor/dispatch"
    }



    @RequestMapping("/usr/vendor/order")
    fun showOrder(model: Model): String {
        // 컴파일러가 추천하는 방식이라 매개변수 명도 넣어줌. Boolean 값이 두개라 헷갈릴까봐 이쪽을 추천하는듯?
        if(rq.getLoginedMember()!!.extra__roleCategoryId == 1){
            val flowerOrders = vendorService.getOrdersByVendorMemberIdAndOrderStatus(rq.getLoginedMember()!!.id, rq.getLoginedMember()!!.extra__roleCategoryId!!,
                orderStatus = true,
                completionStatus = false,
                "flower"
            )
            val flowerTributeOrders = vendorService.getOrdersByVendorMemberIdAndOrderStatus(rq.getLoginedMember()!!.id, rq.getLoginedMember()!!.extra__roleCategoryId!!,
                orderStatus = true,
                completionStatus = false,
                "flowerTribute"
            )

            model.addAttribute("flowerOrders", flowerOrders)
            model.addAttribute("flowerTributeOrders", flowerTributeOrders)
        }else if(rq.getLoginedMember()!!.extra__roleCategoryId == 2){

        }

        return "usr/vendor/order"
    }

    @RequestMapping("/usr/vendor/doRequest", method = [RequestMethod.POST])
    @ResponseBody
    fun doRequest(
        multipartRequest: MultipartRequest
    ): String {
        vendorRequest(multipartRequest, 1)

        return rq.replaceJs("제단꽃 공급업자 등록 신청이 완료되었습니다.", "../home/main")
    }

    @RequestMapping("/usr/vendor/doMourningClothRequest", method = [RequestMethod.POST])
    @ResponseBody
    fun doMourningClothRequest(
        multipartRequest: MultipartRequest
    ): String {
        vendorRequest(multipartRequest, 2)

        return rq.replaceJs("상복 공급업자 등록 신청이 완료되었습니다.", "../home/main")
    }

    @RequestMapping("/usr/vendor/doCoffinTransporterRequest", method = [RequestMethod.POST])
    @ResponseBody
    fun doCoffinTransporterRequest(
        multipartRequest: MultipartRequest
    ): String {
        vendorRequest(multipartRequest, 3)

        return rq.replaceJs("운구차량 사업자 등록 신청이 완료되었습니다.", "../home/main")
    }

    @RequestMapping("/usr/vendor/doShroudRequest", method = [RequestMethod.POST])
    @ResponseBody
    fun doShroudRequest(
        multipartRequest: MultipartRequest
    ): String {
        vendorRequest(multipartRequest, 4)

        return rq.replaceJs("수의 사업자 등록 신청이 완료되었습니다.", "../home/main")
    }

    @RequestMapping("/usr/vendor/doDispatch", method = [RequestMethod.POST])
    @ResponseBody
    fun doDispatch(
        clientId: Int
    ): String {
        val detail = "flower"
        return Ut.getJsonStrFromObj(vendorService.modifyOrderIntoVendorMemberIdAndOrderStatusByDirectorMemberIdAndDetail(rq.getLoginedMember()!!.id, clientId, detail))
    }

    @RequestMapping("/usr/vendor/doComplete", method = [RequestMethod.POST])
    @ResponseBody
    fun doComplete(
        funeralId: Int
    ): String {
        return Ut.getJsonStrFromObj(vendorService.modifyOrderIntoCompleteStatusByVendorMemberIdAndClientId(rq.getLoginedMember()!!.id, funeralId, true))
    }

    fun vendorRequest(multipartRequest: MultipartRequest, roleCategoryId: Int){
        val fileMap = multipartRequest.fileMap
        for (fileInputName in fileMap.keys) {
            val multipartFile = fileMap[fileInputName]

            if (multipartFile != null) {
                // 파일을 저장야
                genFileService.save(multipartFile, rq.getLoginedMember()!!.id)
            }

//      제단꽃 공급업자는 자기소개(introduce)가 필요없어서 공백을 넣는다.
//      물품 공급업자의 roleLevel(roleId)는 4
            val introduce = ""
            memberRoleService.insertMemberRole(introduce, rq.getLoginedMember()!!.id, 4, roleCategoryId)
            memberService.modifyMemberIntoRoleLevelByMemberId(rq.getLoginedMember()!!.id, 4)
        }

        // 물품 공급업자 등록 승인 요청 시, 로그인 세션 데이터를 바꿔줘야한다.
        rq.login(memberService.getMemberById(rq.getLoginedMember()!!.id)!!)
    }
}