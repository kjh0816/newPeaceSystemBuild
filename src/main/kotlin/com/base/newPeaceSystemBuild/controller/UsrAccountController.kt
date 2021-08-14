package com.base.newPeaceSystemBuild.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class UsrAccountController {

    @RequestMapping("/usr/account/home")
    fun showHome(): String{

        return "usr/home/main"
    }
}