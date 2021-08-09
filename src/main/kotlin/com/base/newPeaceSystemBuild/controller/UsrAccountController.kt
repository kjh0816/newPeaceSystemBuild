package com.base.newPeaceSystemBuild.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody


@Controller
class UsrAccountController {

    @RequestMapping("/usr/account/home")
    @ResponseBody
    fun showHome(): String{

        println("안녕하소")
        return "안녕하소"

    }
}