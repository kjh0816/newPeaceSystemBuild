package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.*
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.client.Client
import com.base.newPeaceSystemBuild.vo.standard.Flower
import com.base.newPeaceSystemBuild.vo.standard.MourningCloth
import com.base.newPeaceSystemBuild.vo.vendor.Order
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


        model.addAttribute("flowers", flowers)

        return "usr/vendor/request"
    }

    @RequestMapping("/usr/vendor/mourningClothRequest")
    fun showMourningClothRequest(model: Model): String{

        val femaleMourningCloths: List<MourningCloth> = vendorService.getFemaleMourningCloths()
        val maleMourningCloths: List<MourningCloth> = vendorService.getMaleMourningCloths()
        val shirts: List<MourningCloth> = vendorService.getShirts()
        val neckties: List<MourningCloth> = vendorService.getNeckties()


        model.addAttribute("femaleMourningCloths", femaleMourningCloths)
        model.addAttribute("maleMourningCloths", maleMourningCloths)
        model.addAttribute("shirts", shirts)
        model.addAttribute("neckties", neckties)

        return "usr/vendor/mourningClothRequest"
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
        val flowerTributeOrder = vendorService.getOrderByDirectorMemberIdAndCompletionStatusAndDetail(directorMemberId, false, "flowerTribute")

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
        clientId: Int
    ): String {
        return Ut.getJsonStrFromObj(vendorService.modifyOrderIntoCompleteStatusByVendorMemberIdAndClientId(rq.getLoginedMember()!!.id, clientId, true))
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