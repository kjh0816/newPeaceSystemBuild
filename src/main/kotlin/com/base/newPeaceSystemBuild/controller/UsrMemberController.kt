package com.base.newPeaceSystemBuild.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody


@Controller
class UsrMemberController {

    @RequestMapping("/usr/member/login")
    @ResponseBody
    fun showLogin(): String{
        return "Login Page"
    }
}