package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.GenFileService
import com.base.newPeaceSystemBuild.service.MemberService
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.member.Member
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import kotlin.math.ceil


@Controller
class AdmHomeController(
    private val memberService: MemberService,
    private val genFileService: GenFileService
) {
    @Autowired
    private lateinit var rq: Rq;

    // VIEW Mapping 함수 시작
    @RequestMapping("/adm/home/main")
    fun showMain(model: Model, authenticationLevel: Int, roleLevel: Int, page: Int): String {
        // Member 테이블에서 roleLevel 과 authenticationLevel 이 일치하는 데이터들만 추려서 가져온다.
        val members = memberService.getMembersByRoleLevelAndAuthenticationLevel(roleLevel, authenticationLevel)

        val itemsInAPage = 5

        val limitFrom = (page - 1) * itemsInAPage

        // 파라미터로 받은 roleLevel, authenticationLevel 를 기준으로 page 별로 필터링된 회원정보를 가져온다.
        val filteredMembers = memberService.getFilteredMembers(roleLevel, authenticationLevel, page, itemsInAPage, limitFrom)

        val totalPage = ceil(members.size.toDouble() / itemsInAPage).toInt()

        for (member in filteredMembers) {
            // 중복코드 발생으로 인한 객체화
            setExtra__thumbnailImgUrl(member, roleLevel)
            member.cellphoneNo = memberService.getCellphoneNoFormatted(member.id)
        }

        // 총 회원수는 관리자(1명)을 제외한 나머지
        val totalMembersCount = memberService.getMembers()?.size?.minus(1)

        model.addAttribute("totalMembersCount", totalMembersCount)
        model.addAttribute("members", filteredMembers)
        model.addAttribute("page", page)
        model.addAttribute("totalPage", totalPage)

        return "adm/home/main"
    }

    @RequestMapping("/adm/home/detail")
    fun showDetail(model: Model, memberId: Int, roleLevel: Int): String {
        val member = memberService.getMemberById(memberId)
        val members = memberService.getMembersByRoleLevel(roleLevel)

        if(member != null){
            // 중복코드 발생으로 인한 객체화
            setExtra__thumbnailImgUrl(member, roleLevel)
            member.cellphoneNo = memberService.getCellphoneNoFormatted(member.id)
        }
        model.addAttribute("member", member)
        model.addAttribute("members", members)

        return "adm/home/detail"
    }
    // VIEW Mapping 함수 끝

    // VIEW 기능 함수 시작
    @RequestMapping("/adm/home/doApproval", method = [RequestMethod.POST])
    @ResponseBody
    fun doApproval(
        memberId: Int,
        authenticationLevel: Int,
        roleLevel: Int,
    ): String {
        // 장례지도사 신청시 생긴 MemberRole 테이블의 authenticationLevel를 파라미터로 받아온 authenticationLevel로 수정한다.
        // 수정 대상은 WHERE 절로 memberId를 통해 선택한다.
        memberService.modifyMemberRoleIntoAuthenticationLevelAndRoleLevelByMemberId(memberId, authenticationLevel, roleLevel)

        var replaceUri =  "main?authenticationLevel=0&roleLevel=3&page=1"
        // authenticationLevel이 0인 상태: 기본값 (requestStatus가 1인 경우, 승인 대기 리스트 / requestStatus가 0인 경우는 등록 신청을 안 한 상태)
        // authenticationLevel이 1인 상태: 승인된 상태
        // authenticationLevel이 2인 상태: 보류된 상태
        if(authenticationLevel == 1){
            if(roleLevel == 3){
                replaceUri = "main?authenticationLevel=$authenticationLevel&roleLevel=$roleLevel&page=1"
                return rq.replaceJs("장례지도사 승인완료.", replaceUri)
            }
            else if(roleLevel == 4){
                replaceUri = "main?authenticationLevel=$authenticationLevel&roleLevel=$roleLevel&page=1"
                return rq.replaceJs("물품 공급업자 승인완료.", replaceUri)
            }
        }
        else if(authenticationLevel == 2){
            if(roleLevel == 3){
                replaceUri = "main?authenticationLevel=$authenticationLevel&roleLevel=$roleLevel&page=1"
                return rq.replaceJs("장례지도사 보류완료.", replaceUri)
            }
            else if(roleLevel == 4){
                replaceUri = "main?authenticationLevel=$authenticationLevel&roleLevel=$roleLevel&page=1"
                return rq.replaceJs("물품 공급업자 보류완료.", replaceUri)
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
    @RequestMapping("/adm/home/getPage", method = [RequestMethod.POST])
    @ResponseBody
    fun getPage(
        model: Model, authenticationLevel: Int, roleLevel: Int, page: Int
    ): String {
        // Member 테이블에서 roleLevel 과 authenticationLevel 이 일치하는 데이터들만 추려서 가져온다.
        val members = memberService.getMembersByRoleLevelAndAuthenticationLevel(roleLevel, authenticationLevel)

        val itemsInAPage = 5

        val limitFrom = (page - 1) * itemsInAPage

        // 파라미터로 받은 roleLevel, authenticationLevel 를 기준으로 page 별로 필터링된 회원정보를 가져온다.
        val filteredMembers = memberService.getFilteredMembers(roleLevel, authenticationLevel, page, itemsInAPage, limitFrom)

        val totalPage = ceil(members.size.toDouble() / itemsInAPage).toInt()

        for (member in filteredMembers) {
            // 중복코드 발생으로 인한 객체화
            setExtra__thumbnailImgUrl(member, roleLevel)
        }

        model.addAttribute("members", filteredMembers)
        model.addAttribute("page", page)
        model.addAttribute("totalPage", totalPage)

        return Ut.getJsonStrFromObj(memberService.getForPrintPageMember(roleLevel, authenticationLevel, page, itemsInAPage, limitFrom))
    }
}