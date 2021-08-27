package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.MemberService
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.member.Department
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
        @RequestParam(defaultValue = "") loginPwInput: String,
        @RequestParam(defaultValue = "../home/main") replaceUri: String
    ): String {

//      Ajax 요청을 ResultData 형식으로 응답한다.(Json 형식이므로, 값을 Ajax(JS)로 다룰 수 있다.)

        return Ut.getJsonStrFromObj(memberService.doLogin(loginId, loginPwInput, replaceUri))

    }

    @RequestMapping("/usr/member/doLogout")
    @ResponseBody
    fun doLogout(session: HttpSession) {
//      로그인 세션 해제
        rq.logout()

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


        return Ut.getJsonStrFromObj(memberService.doJoin(loginId, loginPwInput, name, cellphoneNo, email, location, bank, accountNum))
    }

    @RequestMapping("/usr/member/info")
    fun showInfo(model: Model): String {


    // 회원 정보 페이지에서 하이픈이 중간에 들어간 형태로 핸드폰 번호를 보여주기 위해서 따로 출력해서 넘겨준다.
    // 현재 로그인된 회원의 memberId를 넘겨준다.
        val cellphoneNo = memberService.getCellphoneNoFormatted(rq.getLoginedMember()!!.id)

        model.addAttribute("cellphoneNo", cellphoneNo)

        return "usr/member/info"
    }


    @RequestMapping("/usr/member/call")
    fun showCall(model: Model): String{

        val departments: List<Department> = memberService.getDepartments()

        model.addAttribute("departments", departments)


        return "usr/member/call"
    }


    @RequestMapping("/usr/member/doCall")
    @ResponseBody
    fun doCall(
        @RequestParam(defaultValue = "") deceasedName: String,
        @RequestParam(defaultValue = "") relatedName: String,
        @RequestParam(defaultValue = "") cellphoneNo: String,
        @RequestParam(defaultValue = "") location: String,
        @RequestParam(defaultValue = "") address: String
    ): String{

        val memberId = rq.getLoginedMember()!!.id



        return Ut.getJsonStrFromObj(memberService.insertIntoClient(
            memberId, deceasedName, relatedName, cellphoneNo, location, address))
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