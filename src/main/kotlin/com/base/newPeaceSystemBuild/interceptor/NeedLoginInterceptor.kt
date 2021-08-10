package com.base.newPeaceSystemBuild.interceptor

import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class NeedLoginInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var rq: Rq;

    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        println("로그인 인터셉터 실행")
        if (!rq.isLogined()) {
            rq.respUtf8()
            rq.printReplaceJs("로그인 후 이용해주세요.", "../../usr/member/login?afterLoginUri=${rq.getEncodedAfterLoginUri()}")

            return false
        }

        return super.preHandle(req, resp, handler)
    }
}