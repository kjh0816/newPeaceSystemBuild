package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.MemberService
import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody


@Controller
class AdmHomeController(
    private val memberService: MemberService
) {
    @Autowired
    private lateinit var rq: Rq;

    // VIEW Mapping 함수 시작
    @RequestMapping("/adm/home/main")
    fun showMain(model: Model, authenticationStatus: Int): String {
        val membersByAuthenticationStatus = memberService.getMembersByAuthenticationStatus(authenticationStatus)

        model.addAttribute("membersByAuthenticationStatus", membersByAuthenticationStatus)

        return "adm/home/main"
    }
    // VIEW Mapping 함수 끝

    // VIEW 기능 함수 시작
    @RequestMapping("/adm/home/doApproval", method = [RequestMethod.POST])
    @ResponseBody
    fun doApproval(memberId: Int, authenticationStatus: Int): String {
        memberService.updateAuthenticationStatus(memberId, authenticationStatus)
        if(authenticationStatus == 1){
            return rq.replaceJs("장례지도사 승인완료.", "main?authenticationStatus=$authenticationStatus")
        }
        else if(authenticationStatus == 2){
            return rq.replaceJs("장례지도사 보류완료.", "main?authenticationStatus=$authenticationStatus")
        }
        return "redirect:"
    }
    // VIEW 기능 함수 끝
}