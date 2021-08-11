package com.base.newPeaceSystemBuild.vo

import com.base.newPeaceSystemBuild.util.Ut
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

// Componet를 지정해주면 view 파일에서 @rq.~로 접근할 수 있다.(rq 객체를 setAttribute 해주지 않아도 된다.)
@Component
// HTTPS request 객체당 하나의 빈을 return
// request 단위(사용자마다) 다른 rq 클래스를 참조한다.
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
class Rq {

    private lateinit var req: HttpServletRequest
    private lateinit var resp: HttpServletResponse

    private var isLogined: Boolean = false
    private var loginedMember: Member? = null

    fun initWith(req: HttpServletRequest, resp: HttpServletResponse) {
        this.req = req
        this.resp = resp

//        setCurrentLoginInfo(req.session)
    }

    fun isLogined(): Boolean {
        return this.isLogined
    }

    fun getLoginedMember(): Member? {
        return loginedMember
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

    fun printReplaceJs(msg: String, uri: String) {
        this.print(replaceJs(msg, uri))
    }

    fun print(str: String) {
        resp.writer.print(str)
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

    // 현재 URI 가져오는 함수    
    fun getCurrentUri(): String {
        var uri = req.requestURI
        val queryStr = req.queryString
        if (queryStr != null && queryStr.length > 0) {
            uri += "?$queryStr"
        }
        return uri
    }

    // 로그인 후 이동할 URI 가져오는 함수
    fun getAfterLoginUri(): String {
        val afterLoginUri: String = getStrParam("afterLoginUri", "")
        return if (afterLoginUri.length > 0) {
            afterLoginUri
        } else getCurrentUri()
    }

    // 현재 URI 인코딩 함수
    fun getEncodedCurrentUri(): String {
        return Ut.getUriEncoded(getCurrentUri())
    }

    // 로그인 후 이동할 URI 인코딩 함수
    fun getEncodedAfterLoginUri(): String {
        return Ut.getUriEncoded(getAfterLoginUri())
    }

    // 파라미터 가져오는 함수
    private fun getStrParam(paramName: String, default: String): String {
        if ( req.getParameter(paramName) == null ) {
            return default
        }

        return req.getParameter(paramName)
    }

    private fun getIntParam(paramName: String, default: Int): Int {
        return if (req.getParameter(paramName) != null) {
            try {
                req.getParameter(paramName)!!.toInt()
            } catch (e: NumberFormatException) {
                default
            }
        } else {
            default
        }
    }
    
    // HttpServletResponse 요청 인코딩 함수 (alert 창 한글 깨짐)
    fun respUtf8() {
        resp.characterEncoding = "UTF-8"
        resp.contentType = "text/html; charset=UTF-8"
    }

}