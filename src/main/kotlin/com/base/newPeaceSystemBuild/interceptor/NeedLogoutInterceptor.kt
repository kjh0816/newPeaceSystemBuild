package com.base.newPeaceSystemBuild.interceptor

import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class NeedLogoutInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var rq: Rq;

    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        //          블랙 리스트 방식
        if (rq.isLogined()) {
            rq.respUtf8()
            rq.printReplaceJs("로그아웃 후 이용해주세요.", "/usr/account/home")

            return false
        }

        return super.preHandle(req, resp, handler)
    }
}