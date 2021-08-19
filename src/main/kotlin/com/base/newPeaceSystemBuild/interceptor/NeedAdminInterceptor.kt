package com.base.newPeaceSystemBuild.interceptor

import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class NeedAdminInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var rq: Rq;

    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        val loginedMember = rq.getLoginedMember()!!
        //          화이트 리스트 방식
        if (loginedMember.roleLevel != 1) {
            rq.respUtf8()
            rq.printReplaceJs("관리자가 아닙니다. 관리자로 로그인 후 다시시도해주세요.", "/usr/home/main")

            return false
        }

        return super.preHandle(req, resp, handler)
    }
}