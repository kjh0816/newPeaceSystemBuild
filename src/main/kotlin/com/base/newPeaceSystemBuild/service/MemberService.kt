package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.MemberRepository
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.ResultData
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.member.Bank
import com.base.newPeaceSystemBuild.vo.member.Department
import com.base.newPeaceSystemBuild.vo.member.Member
import com.base.newPeaceSystemBuild.vo.member.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture as CompletableFuture

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val emailService: EmailService
) {
    @Autowired
    private lateinit var rq: Rq;



    fun getMemberByLoginId(loginId: String): Member? {
        return memberRepository.getMemberByLoginId(loginId)
    }

    fun getMemberByEmail(email: String): Member? {
        return memberRepository.getMemberByEmail(email)
    }

    fun getMemberById(id: Int): Member? {
        return memberRepository.getMemberById(id)
    }

    fun getMembersByAuthenticationLevel(authenticationLevel: Int): List<Member>? {
        return memberRepository.getMembersByAuthenticationLevel(authenticationLevel)
    }

    fun getMembersByRoleLevel(roleLevel: Int): List<Member>? {
        return memberRepository.getMembersByRoleLevel(roleLevel)
    }

    fun getMembersByRoleLevelAndAuthenticationLevel(roleLevel: Int, authenticationLevel: Int): List<Member> {
        return memberRepository.getMembersByRoleLevelAndAuthenticationLevel(roleLevel, authenticationLevel)
    }

    fun join(
        loginId: String,
        loginPw: String,
        name: String,
        cellphoneNo: String,
        email: String,
        location: String,
        bank: String,
        accountNum: String
    ) {
        memberRepository.join(loginId, loginPw, name, cellphoneNo, email, location, bank, accountNum)
    }

    fun getRoles(): List<Role> {
        return memberRepository.getRoles()
    }

    fun getDepartments(): List<Department> {
        return memberRepository.getDepartments()
    }

    fun getBanks(): List<Bank> {
        return memberRepository.getBanks()
    }

    fun isUsableEmail(regex: String, email: String): ResultData {

        if(!Ut.match(regex, email)){
            return ResultData.from("F-1", "이메일 형식에 맞게 입력해주세요.")
        }


        // rq.getLoginedMember()!!.email 를 map으로 반환하는 것은 수정 페이지에서 기존 이메일인 경우에 대응하기 위함.
        // 기존 방식처럼 S-1 일 경우에 rq.getLoginedMember()!!.email 를 return 해버릴경우 정보수정은 문제없지만
        // 회원 가입할땐 rq.getLoginedMember()가 null이기 때문에 에러가난다. 회원가입, 정보수정 모두 에러나지 않게 새로운 if문추가
        val member = getMemberByEmail(email)
        if(member != null){
            if(rq.getLoginedMember() == null){
                return ResultData.from("F-2", "해당 이메일로 가입된 회원이 이미 존재합니다.")
            }
            if(email == rq.getLoginedMember()!!.email){
                return ResultData.from("S-2", "기존 이메일을 유지합니다.", "email", rq.getLoginedMember()!!.email)
            }

            return ResultData.from("F-2", "해당 이메일로 가입된 회원이 이미 존재합니다.")
        }

        return ResultData.from("S-1", "사용 가능한 이메일입니다.")

    }

    fun isUsableLoginId(regex: String, loginId: String): ResultData {
        if(!Ut.match(regex, loginId)){
            return ResultData.from("F-1", "6~20자의 영문 소문자, 숫자만 가능합니다.")
        }

        val member = getMemberByLoginId(loginId)
        if(member != null){
            return ResultData.from("F-2", loginId+"는(은) 이미 존재하는 로그인 아이디입니다.")
        }

        return ResultData.from("S-1", "사용 가능한 로그인 아이디입니다.")
    }

    fun modifyMemberRoleIntoAuthenticationLevelAndRoleLevelByMemberId(memberId: Int, authenticationLevel: Int, roleLevel: Int) {
        memberRepository.modifyMemberRoleIntoAuthenticationLevelAndRoleLevelByMemberId(memberId, authenticationLevel, roleLevel)
    }

    fun modifyMemberIntoRoleLevelByMemberId(memberId: Int, roleLevel: Int) {
        memberRepository.modifyMemberIntoRoleLevelByMemberId(memberId, roleLevel)
    }

    fun modifyMemberIntoRequestStatusByMemberId(memberId: Int, requestStatus: Boolean){
        memberRepository.modifyMemberIntoRequestStatusByMemberId(memberId, requestStatus)
    }

    fun getCellphoneNoFormatted(id: Int): String {
        return memberRepository.getCellphoneNoFormatted(id)
    }

    fun getMembers(): List<Member>? {
        return memberRepository.getMembers()
    }

    fun doLogin(loginId: String, loginPwInput: String, replaceUri: String): ResultData {

        if(loginId.isEmpty()){
            return ResultData.from("F-1", "아이디를 입력해주세요.")
        }
        if(loginPwInput.isEmpty()){
            return ResultData.from("F-2", "비밀번호를 입력해주세요.")
        }
        val member = getMemberByLoginId(loginId)
            ?: return ResultData.from("F-3", "입력하신 정보가 일치하지 않습니다.")
        if ( member.loginPw != loginPwInput ) {
            return ResultData.from("F-3", "입력하신 정보가 일치하지 않습니다.")
        }

        rq.login(member)

        return ResultData.from("S-1", "","replaceUri", replaceUri)
    }

    fun doJoin(
        loginId: String,
        loginPw: String,
        name: String,
        cellphoneNo: String,
        email: String,
        location: String,
        bank: String,
        accountNum: String
    ): ResultData {

        if(loginId.isEmpty()){
            return ResultData.from("F-1", "사용하실 아이디를 입력해주세요.")
        }
        if(loginPw.isEmpty()){
            return ResultData.from("F-2", "사용하실 비밀번호를 입력해주세요.")
        }
        if(name.isEmpty()){
            return ResultData.from("F-3", "이름을 입력해주세요.")
        }
        if(location.isEmpty()){
            return ResultData.from("F-4", "지역을 선택해주세요.")
        }
        if(bank.isEmpty()){
            return ResultData.from("F-5", "은행을 선택해주세요.")
        }
        if(accountNum.isEmpty()){
            return ResultData.from("F-6", "계좌번호를 입력해주세요.")
        }


        val member = getMemberByLoginId(loginId)

        if(member != null){
            return ResultData.from("F-7", "이미 존재하는 로그인 아이디입니다.")
        }

        join(loginId, loginPw, name, cellphoneNo, email, location, bank, accountNum)

        return ResultData.from("S-1", "회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.")
    }

    fun getFilteredMembers(roleLevel: Int, authenticationLevel: Int, page: Int, itemsInAPage: Int, limitFrom: Int): List<Member> {
        return memberRepository.getFilteredMembers(roleLevel, authenticationLevel, page, itemsInAPage, limitFrom)
    }

    fun getForPrintPageMember(
        roleLevel: Int,
        authenticationLevel: Int,
        page: Int,
        itemsInAPage: Int,
        limitFrom: Int
    ): ResultData {
        val members = getFilteredMembers(roleLevel, authenticationLevel, page, itemsInAPage, limitFrom)

        val replaceUri = "/adm/home/main?roleLevel=$roleLevel&authenticationLevel=$authenticationLevel&page=$page"

        return ResultData.from("S-1", "${page}번 페이지를 불러옵니다.", "replaceUri", replaceUri)

    }

    fun isCorrectLoginPw(loginPwInput: String, action: String): ResultData {
        if(loginPwInput.isEmpty()){
            return ResultData.from("F-1", "비밀번호를 입력해주세요.")
        }
        if(rq.getLoginedMember()!!.loginPw != loginPwInput){
            return ResultData.from("F-2", "비밀번호가 일치하지 않습니다.")
        }
        return ResultData.from("S-1", "비밀번호가 일치합니다.", "action", action)
    }

    fun modifyPw(loginPwInput: String): ResultData {
        if(loginPwInput.isEmpty()){
            return ResultData.from("F-1", "비밀번호를 입력해주세요.")
        }

        // 비밀번호를 변경해주고 현재 세션을 재설정한다.
        memberRepository.modifyPw(loginPwInput, rq.getLoginedMember()!!.id)
        rq.login(getMemberById(rq.getLoginedMember()!!.id)!!)

        return ResultData.from("S-1", "비밀번호가 변경되었습니다.")

    }

    fun modifyInfo(cellphoneNo: String, email: String, location: String, bank: String, accountNum: String): ResultData {
        if(cellphoneNo.isEmpty()){
            return ResultData.from("F-1", "핸드폰 번호를 입력해주세요.")
        }
        if(email.isEmpty()){
            return ResultData.from("F-2", "이메일을 입력해주세요.")
        }
        if(location.isEmpty()){
            return ResultData.from("F-3", "지역을 선택해주세요.")
        }
        if(bank.isEmpty()){
            return ResultData.from("F-4", "은행을 선택해주세요.")
        }
        if(accountNum.isEmpty()){
            return ResultData.from("F-5", "계좌번호를 입력해주세요.")
        }

        // 회원정보를 변경해주고 현재 세션을 재설정한다.
        memberRepository.modifyInfo(cellphoneNo, email, location, bank, accountNum, rq.getLoginedMember()!!.id)
        rq.login(getMemberById(rq.getLoginedMember()!!.id)!!)

        return ResultData.from("S-1", "${rq.getLoginedMember()!!.name}님의 정보가 수정되었습니다.")

    }

    fun findIdByNameAndEmail(name: String, email: String): ResultData {

        if(name.isEmpty()){
            return ResultData.from("F-2", "성함을 입력해주세요.")
        }
        if(email.isEmpty()){
            return ResultData.from("F-2", "이메일을 입력해주세요.")
        }

        val member = memberRepository.getMemberByNameAndEmail(name, email)
                ?: return ResultData.from("F-3", "입력하신 정보와 일치하는 회원이 존재하지 않습니다.")

        return ResultData.from("S-1", "회원님의 아이디를 찾았습니다. 이제 비밀번호만 입력하시면 됩니다.", "member", member)


    }

    fun findPwByLoginIdAndEmail(loginId: String, email: String): ResultData {
        if(loginId.isEmpty()){
            return ResultData.from("F-1", "로그인 아이디를 입력해주세요.")
        }
        if(email.isEmpty()){
            return ResultData.from("F-2", "이메일을 입력해주세요.")
        }

        val member = memberRepository.getMemberByLoginId(loginId)
        if(member == null){
            return ResultData.from("F-3", "입력하신 정보와 일치하는 회원이 존재하지 않습니다.")
        }

        // 로그인 아이디로 조회된 회원은 존재하나, 이메일이 일치하지 않는 경우에 대한 예외처리
        if(member.email != email){
            return ResultData.from("F-4", "입력하신 정보와 일치하는 회원이 존재하지 않습니다.")
        }

        // 조회된 회원의 비밀번호를 변경한다.
        val tempPw = Ut.getTempPassword(8)
        memberRepository.changeLoginPwToTempPw(member.id, tempPw)

        val title = "${member.name}님의 newPeace 사이트 임시 비밀번호"
        val body = """${member.name}님의 임시 비밀번호는 $tempPw 입니다.<br>비밀번호를 변경해주십시오."""

        // 이메일 발송을 다른 쓰레드에서 실행(발송하는데 시간이 10초 정도 걸림)
        val asyncSendEmail = CompletableFuture.runAsync {
            emailService.send(member.email, title, body)
        }


        return ResultData.from("S-1", "${member.email}로 임시 비밀번호를 발송합니다. 수신하시기까지 시간이 다소 소요될 수 있습니다.")

    }
}
