package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.ClientService
import com.base.newPeaceSystemBuild.service.MemberService
import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class UsrHomeController(
    private val memberService: MemberService,
    private val clientService: ClientService
) {
    @Autowired
    private lateinit var rq: Rq;

    // VIEW Mapping 함수 시작
    @RequestMapping("/usr/home/main")
    fun showMain(model: Model): String {
        if(rq.isLogined()){
            // 승인이 완료된 장례지도사일 경우
            if(rq.getLoginedMember()!!.roleLevel == 3 && rq.getLoginedMember()!!.extra__authenticationLevel == 1){
                // 로그인 회원 정보의 ID 값으로 funeral 테이블에 directorMemberId 칼럼과 일치하며, progress 칼럼이 true 인 데이터가 있으면 가져온다.
                val progressingFuneral = clientService.getProgressingFuneralByIdDirectorMemberId(rq.getLoginedMember()!!.id)

                if(progressingFuneral != null){
                    // 메인페이지에서 장례지도사가 현재 본인이 진행중인 장례가 있다면 그 정보를 가진 페이지로 이동하기위해 attribute로 세팅해준다
                    model.addAttribute("progressingFuneralClientId", progressingFuneral.clientId)
                }
                else{
                    // else 부분은 없어도 사실 상관없다. moveProgress 에서 파라미터로 받을 때 defaultValue 설정을 0으로 해놨기 때문이다.
                    // 그냥 소스코드 가독성을 위해 추가했다
                    model.addAttribute("progressingFuneralClientId", 0)
                }
            }
        }
        return "usr/home/main"
    }

    @RequestMapping("/usr/home/call")
    fun showCall(): String {
        return "usr/home/call"
    }
    // VIEW Mapping 함수 끝
}