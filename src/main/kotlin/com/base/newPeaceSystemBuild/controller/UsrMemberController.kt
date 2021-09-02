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

    @RequestMapping("/usr/member/requirePw")
    fun showRequirePw(
        model: Model,
        @RequestParam(defaultValue = "") action: String
    ): String {
        // 비밀번호 입력 페이지로 이동

        model.addAttribute("action", action)

        return "usr/member/requirePw"
    }

    @RequestMapping("/usr/member/doRequirePw")
    @ResponseBody
    fun doRequirePw(
        @RequestParam(defaultValue = "") loginPwInput: String,
        @RequestParam(defaultValue = "") action: String
    ): String{
        // 비밀번호 일치 여부에 따라서 Json 데이터 return
        return Ut.getJsonStrFromObj(memberService.isCorrectLoginPw(loginPwInput, action))

    }

    @RequestMapping("/usr/member/modifyPw")
    fun showModifyPw(
        model: Model
    ): String {
        // 비밀번호 수정 페이지로 이동


        return "usr/member/modifyPw"
    }

    @RequestMapping("/usr/member/modifyInfo")
    fun showModifyInfo(
        model: Model
    ): String {
        // 정보 수정 페이지로 이동


        return "usr/member/modifyInfo"
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



//      영업자가 고인 관련 정보를 입력해서 장례지도사 연결 요청
//      신청 시, client 테이블의 directorMemberId = 0
//      장례지도사가 연결되면, 장례지도사 memberId 가 0을 대체한다.

        return Ut.getJsonStrFromObj(memberService.insertIntoClient(
            memberId, deceasedName, relatedName, cellphoneNo, location, address))

    }

    @RequestMapping("/usr/member/progress")
    fun showProgress(
        model: Model,
        @RequestParam(defaultValue = "0") clientId: Int
    ): String{

        // 영업자 본인이 연결한 장례가 어떻게 진행되고 있는지 보여주는 페이지(progress.html)

        // progress 페이지 이동 시, clientId를 파라미터로 받았고,
        // clientId를 통해 고인(client) 테이블의 row를 얻는다.
        val client = memberService.getClientById(clientId)
        // URL로 존재하지 않는 clientId의 접근과 본인이 연결한 client가 아닌 경우에 대한 예외처리를 동시에 한다.
        if(client == null || client.memberId != rq.getLoginedMember()!!.id){

            return "redirect:/usr/home/main"
        }
        // funeral 테이블의 값은 장례지도사가 유족과 연락한 후, 추가적인 정보를 입력했을 때 row가 생성된다.
        // 즉, 처음 funeral 테이블을 조회할 때, 값이 null이다.
        val funeral = memberService.getFuneralById(clientId)

        // 장례지도사가 연결됐을 때, 장례지도사의 이름, 연락처를 영업자에게 보여주기 위함.
        // 장례지도사가 연결되지 않은 상태(funeral 테이블이 null)에서 director를 아래처럼 조회하면 NPE 발생.
        if(funeral != null) {
            val director = memberService.getMemberById(funeral.directorMemberId)
            model.addAttribute("director", director)
        }
        model.addAttribute("client", client)
        model.addAttribute("funeral", funeral)


        return "usr/member/progress"
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