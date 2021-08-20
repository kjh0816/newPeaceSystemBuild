package com.base.newPeaceSystemBuild.interceptor

import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationStatusInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var rq: Rq

    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        //          화이트 리스트 방식
        val loginedMember = rq.getLoginedMember()!!
        rq.respUtf8()
        
        if (loginedMember.extra__authenticationStatus == 1) {
            rq.printReplaceJs("장례지도사 재신청은 할 수 없습니다.", "/usr/home/main")

            return false
        }

        if (loginedMember.extra__authenticationStatus == 3) {
            rq.printReplaceJs("장례지도사 승인 대기중입니다.", "/usr/home/main")

            return false
        }

        if (loginedMember.extra__authenticationStatus == 2) {
            rq.printReplaceJs("장례지도사 임시 보류된 회원입니다.", "/usr/home/main")

            return false
        }

        return super.preHandle(req, resp, handler)
    }
}