package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.MemberService
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.Member
import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpSession


@Controller
class UsrMemberController(
    private val memberService: MemberService
) {
    @Autowired
    private lateinit var rq: Rq;

    @RequestMapping("/usr/member/login")
    fun showLogin(): String {
        return "usr/member/login"
    }

    @RequestMapping("/usr/member/doLogin")
    @ResponseBody
    fun doLogin(loginId: String, loginPw: String, @RequestParam(defaultValue = "../account/home") replaceUri: String): String {
        val member: Member = memberService.getMemberByLoginId(loginId)
            ?: return rq.historyBackJs("존재하지 않는 아이디입니다, 다시 로그인해 주세요.")
        println("멤버 : $member")
        if ( member.loginPw != loginPw ) {
            return rq.historyBackJs("비밀번호가 일치하지 않습니다.")
        }

        rq.login(member)

        return rq.replaceJs("환영합니다.", replaceUri)
    }

    @RequestMapping("/usr/member/doLogout")
    @ResponseBody
    fun doLogout(session: HttpSession): String {
        rq.logout()

        return rq.replaceJs("로그아웃 되었습니다.", "../account/home")
    }
}