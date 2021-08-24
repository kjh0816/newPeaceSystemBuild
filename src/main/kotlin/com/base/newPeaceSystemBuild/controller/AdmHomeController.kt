package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.GenFileService
import com.base.newPeaceSystemBuild.service.MemberService
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.member.Member
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody


@Controller
class AdmHomeController(
    private val memberService: MemberService,
    private val genFileService: GenFileService
) {
    @Autowired
    private lateinit var rq: Rq;

    // VIEW Mapping 함수 시작
    @RequestMapping("/adm/home/main")
    fun showMain(model: Model, authenticationLevel: Int, roleLevel: Int): String {
        // Member 테이블에서 roleLevel 과 authenticationLevel 이 일치하는 데이터들만 추려서 가져온다.
        val members = memberService.getMembersByRoleLevelAndAuthenticationLevel(roleLevel, authenticationLevel)


        if(members != null){
            for (member in members) {
                // 중복코드 발생으로 인한 객체화
                setExtra__thumbnailImgUrl(member, roleLevel)
            }
        }

        model.addAttribute("members", members)

        return "adm/home/main"
    }

    @RequestMapping("/adm/home/detail")
    fun showDetail(model: Model, memberId: Int, roleLevel: Int): String {
        val member = memberService.getMemberById(memberId)

        if(member != null){
            // 중복코드 발생으로 인한 객체화
            setExtra__thumbnailImgUrl(member, roleLevel)
        }
        model.addAttribute("member", member)

        return "adm/home/detail"
    }
    // VIEW Mapping 함수 끝

    // VIEW 기능 함수 시작
    @RequestMapping("/adm/home/doApproval", method = [RequestMethod.POST])
    @ResponseBody
    fun doApproval(memberId: Int, authenticationLevel: Int, roleLevel: Int): String {
        // 장례지도사 신청시 생긴 MemberRole 테이블의 authenticationLevel를 파라미터로 받아온 authenticationLevel로 수정한다.
        // 수정 대상은 WHERE 절로 memberId를 통해 선택한다.
        memberService.modifyMemberRoleIntoAuthenticationLevelByMemberId(memberId, authenticationLevel)
        if(authenticationLevel == 1){
            if(roleLevel == 3){
                return rq.replaceJs("장례지도사 승인완료.", "main?authenticationLevel=$authenticationLevel&roleLevel=$roleLevel")
            }
            else if(roleLevel == 4){
                return rq.replaceJs("물품 공급업자 승인완료.", "main?authenticationLevel=$authenticationLevel&roleLevel=$roleLevel")
            }
        }
        else if(authenticationLevel == 2){
            if(roleLevel == 3){
                return rq.replaceJs("장례지도사 보류완료.", "main?authenticationLevel=$authenticationLevel&roleLevel=$roleLevel")
            }
            else if(roleLevel == 4){
                return rq.replaceJs("물품 공급업자 보류완료.", "main?authenticationLevel=$authenticationLevel&roleLevel=$roleLevel")
            }
        }
        return ""
    }
    // VIEW 기능 함수 끝

    // Controller 내부에서 사용할 함수 시작
    // Member 객체 field 안에있는 extra__thumbnailImgUrl에 값을 할당해주는 함수
    fun setExtra__thumbnailImgUrl(member: Member, roleLevel: Int){
        var genFileTypeCode = ""
        if(roleLevel == 3){
            genFileTypeCode = "director"
        }
        else if(roleLevel == 4){
            genFileTypeCode = "vendor"
        }

        // 매개변수로 넘겨준 5개의 값이 모두 일치하는 데이터를 찾음
        val genFile = genFileService.getGenFile("member", member.id, genFileTypeCode, "attachment", 1)

        if (genFile != null) {
            if(member.id == genFile.relId && genFile.fileNo == 1){
                // Java Setter 랑 같은역할 member 객체에 extra__thumbnailImgUrl 변수에 값 주입
                member.extra__thumbnailImgUrl = genFile.getForPrintUrl()
            }
        }
    }
    // Controller 내부에서 사용할 함수 끝
}