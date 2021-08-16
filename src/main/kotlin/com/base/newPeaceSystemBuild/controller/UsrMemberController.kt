package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.MemberService
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpSession


@Controller
class UsrMemberController(
    private val memberService: MemberService
) {
    @Autowired
    private lateinit var rq: Rq;

    // VIEW Mapping 함수 시작
    @RequestMapping("/usr/member/login")
    fun showLogin(): String {
        return "usr/member/login"
    }

    @RequestMapping("/usr/member/join")
    fun showJoin(model: Model): String {
        val roles = memberService.getRoles()
        val departments = memberService.getDepartments()
        val banks = memberService.getBanks()

        model.addAttribute("roles", roles)
        model.addAttribute("departments", departments)
        model.addAttribute("banks", banks)

        return "usr/member/join"
    }
    // VIEW Mapping 함수 끝

    // VIEW 기능 함수 시작
    @RequestMapping("/usr/member/doLogin", method = [RequestMethod.POST])
    @ResponseBody
    fun doLogin(
        @RequestParam(defaultValue = "") loginId: String,
        @RequestParam(defaultValue = "") loginPw: String,
        @RequestParam(defaultValue = "../home/main") replaceUri: String
    ): String {
        if(loginId.isEmpty()){
            return rq.historyBackJs("아이디를 입력해주세요.")
        }
        if(loginPw.isEmpty()){
            return rq.historyBackJs("비밀번호를 입력해주세요.")
        }

        val member = memberService.getMemberByLoginId(loginId)
            ?: return rq.historyBackJs("존재하지 않는 아이디입니다, 다시 로그인해 주세요.")

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

        return rq.replaceJs("로그아웃 되었습니다.", "../home/main")
    }

    @RequestMapping("/usr/member/doJoin", method = [RequestMethod.POST])
    @ResponseBody
    fun doJoin(
        @RequestParam(defaultValue = "") loginId: String,
        @RequestParam(defaultValue = "") loginPwInput: String,
        @RequestParam(defaultValue = "") name: String,
        @RequestParam(defaultValue = "") cellphoneNo: String,
        @RequestParam(defaultValue = "") email: String,
        @RequestParam(defaultValue = "") location: String,
        @RequestParam(defaultValue = "") bank: String,
        @RequestParam(defaultValue = "") accountNum: String
    ): String {
        if(loginId.isEmpty()){
            return rq.historyBackJs("사용하실 아이디를 입력해주세요.")
        }
        if(loginPwInput.isEmpty()){
            return rq.historyBackJs("사용하실 비밀번호를 입력해주세요.")
        }
        if(name.isEmpty()){
            return rq.historyBackJs("이름을 입력해주세요.")
        }
        if(location.isEmpty()){
            return rq.historyBackJs("지역을 선택해주세요.")
        }
        if(bank.isEmpty()){
            return rq.historyBackJs("은행을 선택해주세요.")
        }
        if(accountNum.isEmpty()){
            return rq.historyBackJs("계좌번호를 입력해주세요.")
        }


        val member = memberService.getMemberByLoginId(loginId)

        if(member != null){
            return rq.historyBackJs("이미 존재하는 로그인 아이디입니다.")
        }

        memberService.join(loginId, loginPwInput, name, cellphoneNo, email, location, bank, accountNum)

        return rq.replaceJs("회원가입이 완료되었습니다, 로그인페이지로 이동합니다.", "./login")
    }
    // VIEW 기능 함수 끝

    // AJax 기능 함수 시작
    @RequestMapping("/usr/member/loginIdCheck", method = [RequestMethod.POST])
    @ResponseBody
    fun loginIdCheck(
        @RequestParam(defaultValue = "") loginId: String
    ): String {
        val regex = "^[a-z0-9]{6,20}\$"

        return Ut.getJsonStrFromObj(memberService.isUsableLoginId(regex, loginId))
    }

    @RequestMapping("/usr/member/emailCheck", method = [RequestMethod.POST])
    @ResponseBody
    fun emailCheck(
        @RequestParam(defaultValue = "") email: String
    ): String {

        val regex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}\$"

        return Ut.getJsonStrFromObj(memberService.isUsableEmail(regex, email))

    }
    // AJax 기능 함수 끝
}