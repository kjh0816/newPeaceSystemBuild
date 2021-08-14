package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class UsrDirectorController(
) {
    @Autowired
    private lateinit var rq: Rq;

    // VIEW Mapping 함수 시작
    @RequestMapping("/usr/director/request")
    fun showLogin(): String {
        return "usr/director/request"
    }
    // VIEW Mapping 함수 끝
}