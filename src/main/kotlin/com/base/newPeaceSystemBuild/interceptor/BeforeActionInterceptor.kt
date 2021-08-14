package com.base.newPeaceSystemBuild.interceptor

import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class BeforeActionInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var rq: Rq

    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {

        rq.initWith(req, resp)

        rq.setLoginInfo(req.session)

        return super.preHandle(req, resp, handler)
    }
}