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


//    메인 페이지에서 버튼은 안 보이지만, url로 접속하는 경우를 방지하기 위한 인터셉터
    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        //          화이트 리스트 방식
        val loginedMember = rq.getLoginedMember()!!
        rq.respUtf8()

        // 관리자가 아직 승인하지 않은 상태
        if (loginedMember.extra__authenticationStatus == 0) {
            rq.printReplaceJs("장례지도사 승인 대기중입니다.", "/usr/home/main")

            return false
        }

        // 장례지도사가 된 경우 (roleLevel == 3 && extra__authenticationStatus == 1)
        if (loginedMember.extra__authenticationStatus == 1) {
            rq.printReplaceJs("이미 장례지도사로 등록되셨습니다.", "/usr/home/main")

            return false
        }
        // 장례지도사로 등록을 거절한 경우(추후 승인해줄 수도 있다.)
        if (loginedMember.extra__authenticationStatus == 2) {
            rq.printReplaceJs("장례지도사 임시 보류된 회원입니다.", "/usr/home/main")

            return false
        }

        return super.preHandle(req, resp, handler)
    }
}