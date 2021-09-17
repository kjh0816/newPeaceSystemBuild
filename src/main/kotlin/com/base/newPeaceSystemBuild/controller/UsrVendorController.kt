package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.*
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.standard.Flower
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


    @RequestMapping("/usr/vendor/dispatch")
    fun showDispatch(model: Model, clientId: Int, funeralId: Int): String {
        val client = clientService.getClientById(clientId)
        val funeral = clientService.getFuneralById(funeralId)

        if(funeral == null){
            return "redirect:/usr/home/main"
        }

        val flower = vendorService.getFlowerById(funeral.flowerId)


        model.addAttribute("client", client)
        model.addAttribute("funeral", funeral)
        model.addAttribute("flower", flower)

        return "usr/vendor/dispatch"
    }



    @RequestMapping("/usr/vendor/order")
    fun showOrder(model: Model): String {
        // 컴파일러가 추천하는 방식이라 매개변수 명도 넣어줌. Boolean 값이 두개라 헷갈릴까봐 이쪽을 추천하는듯?
        val details = mutableListOf<String>()

        if(rq.getLoginedMember()!!.extra__roleCategoryId == 1){
            details.add("flower")
            details.add("flowerTribute")
        }

        for (i in 0 until details.size){
            val orders = vendorService.getOrdersByVendorMemberIdAndOrderStatus(rq.getLoginedMember()!!.id, rq.getLoginedMember()!!.extra__roleCategoryId!!,
                orderStatus = true,
                completionStatus = false,
                details[i]
            )
            model.addAttribute("${details[i]}Orders", orders)
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