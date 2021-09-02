package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.MemberRepository
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.Aligo__send__ResponseBody
import com.base.newPeaceSystemBuild.vo.ResultData
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.client.Client
import com.base.newPeaceSystemBuild.vo.client.Funeral
import com.base.newPeaceSystemBuild.vo.member.Bank
import com.base.newPeaceSystemBuild.vo.member.Department
import com.base.newPeaceSystemBuild.vo.member.Member
import com.base.newPeaceSystemBuild.vo.member.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
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

        val member = getMemberByEmail(email)
        if(member != null){
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

    fun modifyMemberRoleIntoAuthenticationLevelByMemberId(memberId: Int, authenticationLevel: Int) {
        memberRepository.modifyMemberRoleIntoAuthenticationLevelByMemberId(memberId, authenticationLevel)
    }

    fun updateRoleLevel(memberId: Int, authenticationStatus: Int) {
        memberRepository.updateRoleLevel(memberId, authenticationStatus)
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

    fun insertIntoClient(
        memberId: Int,
        deceasedName: String,
        relatedName: String,
        cellphoneNo: String,
        location: String,
        address: String
    ): ResultData {

        if(deceasedName.isEmpty()){
            return ResultData.from("F-1", "고인의 성함을 입력해주세요.")
        }
        if(relatedName.isEmpty()){
            return ResultData.from("F-2", "상주의 성함을 입력해주세요.")
        }
        if(cellphoneNo.isEmpty()){
            return ResultData.from("F-3", "상주의 연락처를 입력해주세요.")
        }
        if(location.isEmpty()){
            return ResultData.from("F-4", "지역을 선택해주세요.")
        }
        if(address.isEmpty()){
            return ResultData.from("F-5", "상세 주소를 입력해주세요.")
        }




        //  client에 대한 데이터를 DB에 저장
        memberRepository.insertIntoClient(memberId, deceasedName, relatedName, cellphoneNo, location, address)
        val clientId = memberRepository.getLastInsertId()

        // 문자 메세지 전달 (시작)
        // client 정보 중, location을 반영해서 해당하는 장례지도사들에 문자 메세지 전달


        // 직업: 장례지도사 ( roleLevel = 3 )
        val roleLevel = 3

        // 직업을 구분하기 위해 roleLevel 지역을 구분하기 위해 location을 매개변수로 받아, members를 출력
        // 추후 범용적으로 이 함수를 사용하기 위해 roleCategoryId를 넣었다. (0일 경우, roleCategoryId가 내부적으로 적용되지 않는다.)
        // 반대로, vendor의 경우, roleCategoryId 값을 넣으면 내부적으로 적용된다.
        val directors: List<Member> = memberRepository.getMembersByLocationAndRole(location, roleLevel, 0)

        // 몇 명의 장례지도사가 조회되었고, 문자가 갈 것인지를 알려주기 위한 변수
        val directorsCount = directors.size
        // location으로 조회했을 때, 해당 지역에 한 명도 없는 경우에 대한 예외처리
        if(directors.isEmpty()){
            return ResultData.from("F-6", "${location}에 등록된 장례지도사가 없습니다.")
        }




        // 발신자 전화번호
        val from = "01049219810"
        // 1명 이상의 수신자 전화번호 ( 알리고 API에서 수신인(receiver)로써 인식 가능한 상태로 넣어주는 함수 )
        // 다른 직업에 대해서도 재사용 가능
        val to = Ut.getCellphoneNosFromMembers(directors)
        // 문자 내용
        val msg = "ㅎㅇ"



        // 알리고 API에서 문자 전송에 필요한 데이터를 넘겨주고, 알리고로부터 반환된 결과값 rb
        val rb: Aligo__send__ResponseBody = Ut.sendSms(from, to.toString(), msg, true)

        println("rb 결과값: $rb")
        println("rb 결과값: $rb")
        println("rb 결과값: $rb")

        return ResultData.from("S-1", "${directorsCount}명의 장례지도사 출동을 요청했습니다..", "from", from, "to", to, "msg", msg, "rb", rb, "clientId", clientId)


        // 문자 메세지 전달 (끝)

        // 연결된 장례지도사에게 고인의 정보를 주기 위해 clientId를 성공 시, 같이 return
//        return ResultData.from("S-1", "${directorsCount}명의 장례지도사 출동을 요청했습니다.",
//            "clientId", clientId)


    }

    fun getFilteredMembers(roleLevel: Int, authenticationLevel: Int, page: Int, itemsInAPage: Int, limitFrom: Int): List<Member> {
        return memberRepository.getFilteredMembers(roleLevel, authenticationLevel, page, itemsInAPage, limitFrom)
    }


    fun getClientById(clientId: Int): Client? {
        return memberRepository.getClientById(clientId)
    }

    fun getClientByIdRd(clientId: Int): ResultData {
        val client = memberRepository.getClientById(clientId) ?: return ResultData.from("F-1", "현재 지도사님이 진행중인 장례가 없습니다.")

        return ResultData.from("S-1", "성공", "client", client)
    }

    fun getFuneralById(clientId: Int): Funeral {
        return memberRepository.getFuneralByClientId(clientId)
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

    fun getProgressingFuneral(directorMemberId: Int): Funeral? {
        return memberRepository.getProgressingFuneral(directorMemberId)
    }

    fun modifyClientIntoDirectorMemberIdByClientId(memberId: Int, clientId: Int): ResultData {
        val client = getClientById(clientId) ?: return ResultData.from("F-1", "고인의 정보가 조회되지않습니다.")

        val funeral = getProgressingFuneralByIdDirectorMemberId(rq.getLoginedMember()!!.id)

        if(funeral != null){
            return ResultData.from("F-2", "이미 진행중이신 장례가 있습니다.")
        }

        memberRepository.modifyClientIntoDirectorMemberIdByClientId(memberId, clientId)

        memberRepository.insertFuneral(client.memberId, client.directorMemberId, client.id)

        return ResultData.from("S-1", "출동요청을 승낙하였습니다.", "client", client)
    }

    private fun getProgressingFuneralByIdDirectorMemberId(directorMemberId: Int): Funeral? {
        return memberRepository.getProgressingFuneralByIdDirectorMemberId(directorMemberId)
    }
}
