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
    fun showMain(model: Model): String {
        val membersByAuthenticationStatus = memberService.getMembersByAuthenticationStatus(0)

        if (membersByAuthenticationStatus != null) {
            model.addAttribute("membersByAuthenticationStatus", membersByAuthenticationStatus)
        }

        return "adm/home/main"
    }
    // VIEW Mapping 함수 끝

    // VIEW 기능 함수 시작
    @RequestMapping("/adm/home/doApproval", method = [RequestMethod.POST])
    @ResponseBody
    fun doApproval(memberId: Int, authenticationStatus: Int): String {
        memberService.updateAuthenticationStatus(memberId, authenticationStatus)
        return rq.replaceJs("승인완료.", "main")
    }
    // VIEW 기능 함수 끝
}