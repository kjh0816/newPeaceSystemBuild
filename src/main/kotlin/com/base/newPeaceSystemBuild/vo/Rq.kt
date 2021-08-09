package com.base.newPeaceSystemBuild.vo

import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

// Componet를 지정해주면 view 파일에서 @rq.~로 접근할 수 있다.(rq 객체를 setAttribute 해주지 않아도 된다.)
@Component
// HTTPS request 객체당 하나의 빈을 return
// requst 단위(사용자마다) 다른 rq 클래스를 참조한다.
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
class Rq {
    private var currentPageCanGoEditCurrentKen: Boolean = false
    private lateinit var req: HttpServletRequest
    private var isLogined: Boolean = false
    private var loginedMember: Member? = null
    private var currentPageCanSaveKen = false

    fun isLogined(): Boolean {
        return this.isLogined
    }

    fun getLoginedMember(): Member? {
        return loginedMember
    }

    fun setCurrentPageCanSaveKen(can: Boolean) {
        this.currentPageCanSaveKen = can
    }

    fun isCurrentPageCanSaveKen(): Boolean {
        return currentPageCanSaveKen
    }

    fun setLoginInfo(session: HttpSession) {
        if (session.getAttribute("loginedMemberJsonStr") == null) {
            return
        }

        val loginedMemberJsonStr = session.getAttribute("loginedMemberJsonStr") as String

        isLogined = true
        loginedMember = Ut.getObjFromJsonStr(loginedMemberJsonStr)
    }

    //  매개변수로 받은 msg를 alert로 보여주고 받은 uri로 replace시킨다.
    fun replaceJs(msg: String, uri: String): String{
        return """
            <script>
            const msg = '${msg}'.trim();
            
            if(msg.length > 0){
                alert(msg);
            }
            
            location.replace('${uri}');
            </script>
        """.trimIndent()
    }
    //   매개변수로 받은 msg를 보여주고 뒤로가기
    fun historyBackJs(msg: String): String {
        return """
            <script>
            const msg = '${msg}'.trim();
            
            if ( msg.length > 0 ) {
                alert(msg);
            }
            
            history.back();
            </script>
        """.trimIndent()
    }

    fun setReq(req: HttpServletRequest) {
        this.req = req

        setLoginInfo(req.session)
    }


    fun login(member: Member) {
        req.session.setAttribute("loginedMemberJsonStr", Ut.getJsonStrFromObj(member))
    }

    fun logout() {
        req.session.removeAttribute("loginedMemberJsonStr")
    }

}