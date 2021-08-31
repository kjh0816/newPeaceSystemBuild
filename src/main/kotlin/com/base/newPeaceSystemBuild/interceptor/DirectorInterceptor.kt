package com.base.newPeaceSystemBuild.interceptor

import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class DirectorInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var rq: Rq

    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        //          화이트 리스트 방식
        rq.respUtf8()

        val loginedMember = rq.getLoginedMember()!!
        val roleName = loginedMember.extra__roleName

        if (loginedMember.roleLevel == 3 && loginedMember.extra__authenticationLevel == 1) {
            return super.preHandle(req, resp, handler)
        }

        if (loginedMember.roleLevel == 3 && loginedMember.extra__authenticationLevel == 2) {
            rq.printReplaceJs("$roleName 등록이 임시 보류된 회원입니다.", "/usr/home/main")

            return false
        }

        rq.printReplaceJs("회원님은 장례지도사가 아닙니다.", "/usr/home/main")

        return false
    }
}