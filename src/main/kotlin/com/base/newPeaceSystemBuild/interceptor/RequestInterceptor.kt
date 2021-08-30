package com.base.newPeaceSystemBuild.interceptor

import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RequestInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var rq: Rq

    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        //          화이트 리스트 방식
        rq.respUtf8()

        val loginedMember = rq.getLoginedMember()!!
        val roleName = loginedMember.extra__roleName

        if(loginedMember.extra__authenticationLevel == 1){
            rq.printReplaceJs("이미 ${roleName}로 등록되셨습니다.", "/usr/home/main")

            return false
        }

        if(loginedMember.extra__authenticationLevel == 2){
            rq.printReplaceJs("$roleName 등록이 임시 보류된 회원입니다.", "/usr/home/main")

            return false
        }

        if(loginedMember.extra__authenticationLevel == 0){
            rq.printReplaceJs("$roleName 승인 대기중입니다.", "/usr/home/main")

            return false
        }

        return super.preHandle(req, resp, handler)
    }
}