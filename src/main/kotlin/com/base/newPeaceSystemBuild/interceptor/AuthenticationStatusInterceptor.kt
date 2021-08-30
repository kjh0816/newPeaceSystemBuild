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

        val roleName = loginedMember.extra__roleName

//      extra__authenticationLevel에 대해 3가지 경우가 있다.
//      extra__authenticationLevel에 값은 승인 상태에 따라 3가지(0, 1, 2)로 나뉜다 (이떄 디폴트값은 0)
//      이때 1번일 경우 승인이 완료된 회원이기때문에 해당 URI로 접속이 가능해야한다. 그렇기때문에 걸러주지않는다
        if (loginedMember.extra__authenticationLevel == 0) {

            rq.printReplaceJs("$roleName 승인 대기중입니다.", "/usr/home/main")

            return false
        }

        if (loginedMember.roleLevel == 3 && loginedMember.extra__authenticationLevel == 2) {
            rq.printReplaceJs("$roleName 등록이 임시 보류된 회원입니다.", "/usr/home/main")

            return false
        }

        if (loginedMember.roleLevel == 4 && loginedMember.extra__authenticationLevel == 2) {
            rq.printReplaceJs("$roleName 등록이 임시 보류된 회원입니다.", "/usr/home/main")

            return false
        }


        return super.preHandle(req, resp, handler)
    }
}