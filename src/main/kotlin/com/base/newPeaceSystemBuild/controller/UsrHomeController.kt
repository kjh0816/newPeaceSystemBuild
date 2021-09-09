package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.ClientService
import com.base.newPeaceSystemBuild.service.MemberService
import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class UsrHomeController(
    private val memberService: MemberService,
    private val clientService: ClientService
) {
    @Autowired
    private lateinit var rq: Rq;

    // VIEW Mapping 함수 시작
    @RequestMapping("/usr/home/main")
    fun showMain(model: Model): String {
        return "usr/home/main"
    }

    @RequestMapping("/usr/home/call")
    fun showCall(): String {
        return "usr/home/call"
    }
    // VIEW Mapping 함수 끝
}