package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.MemberService
import com.base.newPeaceSystemBuild.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody


@Controller
class AdmHomeController(
    private val memberService: MemberService
) {
    @Autowired
    private lateinit var rq: Rq;

    // VIEW Mapping 함수 시작
    @RequestMapping("/adm/home/main")
    fun showMain(model: Model, authenticationLevel: Int): String {
        // Member 테이블에서 authenticationLevel 이 일치하는 데이터들만 추려서 가져온다.
        val members = memberService.getMembersByAuthenticationLevel(authenticationLevel)

        model.addAttribute("members", members)

        return "adm/home/main"
    }
    // VIEW Mapping 함수 끝

    // VIEW 기능 함수 시작
    @RequestMapping("/adm/home/doApproval", method = [RequestMethod.POST])
    @ResponseBody
    fun doApproval(memberId: Int, authenticationLevel: Int): String {
        // 장례지도사 신청시 생긴 MemberRole 테이블의 authenticationLevel를 파라미터로 받아온 authenticationLevel로 수정한다.
        // 수정 대상은 WHERE 절로 memberId를 통해 선택한다.
        memberService.modifyMemberRoleIntoAuthenticationLevelByMemberId(memberId, authenticationLevel)
        if(authenticationLevel == 1){
            return rq.replaceJs("장례지도사 승인완료.", "main?authenticationLevel=$authenticationLevel")
        }
        else if(authenticationLevel == 2){
            return rq.replaceJs("장례지도사 보류완료.", "main?authenticationLevel=$authenticationLevel")
        }
        return "redirect:"
    }
    // VIEW 기능 함수 끝
}