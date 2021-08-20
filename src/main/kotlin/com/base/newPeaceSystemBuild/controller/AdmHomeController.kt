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
    fun showLogin(model: Model): String {
        val directorRequestMembers = memberService.getMembersByAuthenticationStatus(0)

        if (directorRequestMembers != null) {
            model.addAttribute("directorRequestMembers", directorRequestMembers)
        }

        return "adm/home/main"
    }
    // VIEW Mapping 함수 끝

    // VIEW 기능 함수 시작
    @RequestMapping("/adm/home/doApproval", method = [RequestMethod.POST])
    @ResponseBody
    fun doLogin(id: Int, authenticationStatus: Int): String {
        memberService.updateAuthenticationStatus(id, authenticationStatus)
        memberService.updateRoleLevel(id)
        return rq.replaceJs("승인완료.", "main")
    }
    // VIEW 기능 함수 끝
}