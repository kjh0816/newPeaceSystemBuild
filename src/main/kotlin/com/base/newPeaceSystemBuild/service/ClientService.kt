package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.ClientRepository
import com.base.newPeaceSystemBuild.repository.MemberRepository
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.Aligo__send__ResponseBody
import com.base.newPeaceSystemBuild.vo.ResultData
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.client.Client
import com.base.newPeaceSystemBuild.vo.client.Family
import com.base.newPeaceSystemBuild.vo.client.Funeral
import com.base.newPeaceSystemBuild.vo.member.Member
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClientService(
    private val clientRepository: ClientRepository,
    private val memberRepository: MemberRepository
) {
    @Autowired
    private lateinit var rq: Rq;

    fun insertIntoClient(
        memberId: Int,
        deceasedName: String,
        relatedName: String,
        cellphoneNo: String,
        location: String,
        briefAddress: String
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
        if(briefAddress.isEmpty()){
            return ResultData.from("F-5", "상세 주소를 입력해주세요.")
        }

        // 이후 cellphoneNo를 가공할 때, 번호가 잘못되면 format이 불가한 에러가 발생할 수 있다.
        // 010으로 시작하는 11자리의 번호와 011 등 구번호(10자리)만 받을 수 있다.
        if(cellphoneNo.length < 10 || cellphoneNo.length > 11){
            return ResultData.from("F-6", "핸드폰 번호를 확인해주세요.")
        }



        // 문자 메세지 전달 (시작)
        // client 정보 중, location을 반영해서 해당하는 장례지도사들에 문자 메세지 전달


        // 직업: 장례지도사 ( roleLevel = 3 )
        val roleLevel = 3

        // 직업을 구분하기 위해 roleLevel 지역을 구분하기 위해 location을 매개변수로 받아, members를 출력
        // 추후 범용적으로 이 함수를 사용하기 위해 roleCategoryId를 넣었다. (0일 경우, roleCategoryId가 내부적으로 적용되지 않는다.)
        // 반대로, vendor의 경우, roleCategoryId 값을 넣으면 내부적으로 적용된다.
        val directors: List<Member> = memberRepository.getMembersByLocationAndRole(location, roleLevel, 0)

        // location으로 조회했을 때, 해당 지역에 한 명도 없는 경우에 대한 예외처리
        if(directors.isEmpty()){
            return ResultData.from("F-6", "${location}에 등록된 장례지도사가 없습니다.")
        }


        //  client에 대한 데이터를 DB에 저장
        clientRepository.insertIntoClient(memberId, deceasedName, location, briefAddress)
        val clientId = clientRepository.getLastInsertId()

        // 상주를 포함한 고인의 유가족과 관련된 데이터를 담는 테에블에 저장(여기서는 상주에 대한 정보만 들어간다.)
        clientRepository.insertIntoFamily(clientId, relatedName, cellphoneNo)

        // 발신자 전화번호
        val from = "01049219810"
        // 1명 이상의 수신자 전화번호 ( 알리고 API에서 수신인(receiver)로써 인식 가능한 상태로 넣어주는 함수 )
        // 다른 직업에 대해서도 재사용 가능
        val to = Ut.getCellphoneNosFromMembers(directors)
        // 문자 내용
        val msg = "https://webroot/usr/director/dispatch?clientId=${clientId} \n위 링크를 통해 유족의 위치를 확인하시고, 수락해주십시오."

        // 알리고 API에서 문자 전송에 필요한 데이터를 넘겨주고, 알리고로부터 반환된 결과값 rb
        val rb: Aligo__send__ResponseBody = Ut.sendSms(from, to.toString(), msg, true)


        return ResultData.from("S-1", "요청이 완료되었습니다. 입력하신 연락처로 장례지도사가 연락합니다.", "from", from, "to", to, "msg", msg, "rb", rb, "clientId", clientId)


        // 문자 메세지 전달 (끝)

        // 연결된 장례지도사에게 고인의 정보를 주기 위해 clientId를 성공 시, 같이 return
        // return ResultData.from("S-1", "${directorsCount}명의 장례지도사 출동을 요청했습니다.",
        // "clientId", clientId)
    }

    fun getClientById(clientId: Int): Client? {
        return clientRepository.getClientById(clientId)
    }

    fun moveProgressRd(clientId: Int): ResultData {
        val client = clientRepository.getClientById(clientId) ?: return ResultData.from("F-1", "현재 지도사님이 진행중인 장례가 없습니다.")

        return ResultData.from("S-1", "성공", "client", client)
    }

    fun getFuneralByClientId(clientId: Int): Funeral? {
        return clientRepository.getFuneralByClientId(clientId)
    }

    fun getProgressingFuneralByDirectorMemberId(directorMemberId: Int): Funeral? {
        return clientRepository.getProgressingFuneralByDirectorMemberId(directorMemberId)
    }

    fun modifyClientIntoDirectorMemberIdByClientId(directorMemberId: Int, clientId: Int): ResultData {
        // post 요청이라 사용자가 파라미터를 바꿀수 없어 clientId가 잘못들어오는 경우는 없겠지만, null 처리를 해줘야하기 때문에 !!는 불안정한 느낌이라 추가함
        // 영업자가 client 정보를 입력한 시점에서 client 데이터가 누락되는 경우는 없으므로, clientId가 일치하지 않는 경우, 잘못된 URL 접근이므로 예외처리
        val client = getClientById(clientId) ?: return ResultData.from("F-1", "잘못된 접근입니다.")


        // clientId로 장례를 조회한다. (clientId는 중복이 없다.)
        val funeral = getFuneralByClientId(clientId)

        // 조회된 데이터가 있고, 해당 데이터의 directorMemberId 칼럼이 directorMemberId(rq.getLoginedMember()!!.id) 랑 같을경우 본인이 이미 승낙한 장례라는걸 알려줌
        if(funeral != null && funeral.directorMemberId == directorMemberId){

            val replaceUrl = "/usr/director/progress?clientId=$clientId"

            return ResultData.from("F-2", "이미 출동 요청을 승락하셨습니다. 진행 중인 장례 페이지를 확인해주세요.", "replaceUrl", replaceUrl)
        }

        // client 테이블에 directorMemberId가 0이 아니면 장례지도사 배정이 완료된상태
        if(client.directorMemberId != 0){

            val replaceUrl = "/usr/home/main"

            return ResultData.from("F-3", "이미 장례지도사가 배정되었습니다.", "replaceUrl", replaceUrl)
        }
        
        // 위 조건을 만족한 경우, 장례지도사가 등록되지 않은 시점이므로, 해당 client에게 장례지도사 배정
        clientRepository.modifyClientIntoDirectorMemberIdByClientId(directorMemberId, clientId)

        // 장레지도사 배정이 완료된 시점으로, funeral 테이블에 데이터 삽입
        clientRepository.insertFuneral(client.memberId, directorMemberId, client.id)

        // 영업자와 장례지도사에게 장례지도사가 배정된 것에 대한 정보를 문자로 알림(시작)

        // 장례지도사에게 문자 메세지 전달

        val directorRoleLevel = 3

        val director: List<Member> = memberRepository.getMemberByIdForMsg(directorMemberId)

        // 발신자 전화번호
        val from = "01049219810"
        // 1명 이상의 수신자 전화번호 ( 알리고 API에서 수신인(receiver)로써 인식 가능한 상태로 넣어주는 함수 )
        // 다른 직업에 대해서도 재사용 가능
        val to = Ut.getCellphoneNosFromMembers(director)
        // 문자 내용
        val msg = "${rq.getLoginedMember()!!.name} 장례지도사님께서 故 ${client.deceasedName}님의 장례에 배정되었습니다. " +
                "\n아래 링크를 통해 유족과 연락해주시고, 장례를 진행해주십시오." +
                "\nhttps://webroot/usr/director/progress?clientId=${clientId}"

        // 알리고 API에서 문자 전송에 필요한 데이터를 넘겨주고, 알리고로부터 반환된 결과값 rb
        val rb: Aligo__send__ResponseBody = Ut.sendSms(from, to.toString(), msg, true)


        // 영업자에게 문자 메세지 전달

        val memberRoleLevel = 2


        // 수신자를 넣어주는 함수가 List<Member>를 받으므로, 아래 함수를 통해 List<Member> 형태로 불러오는 함수
        // 중복 데이터가 없이 1개의 row만을 얻도록 clientId와 함께 조회

        val member = memberRepository.getMemberByIdForMsg(client.memberId)

        // 발신자 전화번호
        val from2 = "01049219810"
        // 1명 이상의 수신자 전화번호 ( 알리고 API에서 수신인(receiver)로써 인식 가능한 상태로 넣어주는 함수 )
        // 다른 직업에 대해서도 재사용 가능
        val to2 = Ut.getCellphoneNosFromMembers(member)
        // 문자 내용
        val msg2 = "故 ${client.deceasedName}님의 장례를 위한 장례지도사가 배정되었습니다. " +
                "\n진행 상황은 아래 링크를 통해서 확인해주십시오." +
                "\nhttps://webroot/usr/member/progress?clientId=${clientId}"

        // 알리고 API에서 문자 전송에 필요한 데이터를 넘겨주고, 알리고로부터 반환된 결과값 rb
        val rb2: Aligo__send__ResponseBody = Ut.sendSms(from2, to2.toString(), msg2, true)


        // 영업자와 장례지도사에게 장례지도사가 배정된 것에 대한 정보를 문자로 알림(끝)

        return ResultData.from("S-1", "출동 요청을 승낙하였습니다.", "client", client)
    }

    fun getProgressingFuneralByIdDirectorMemberId(directorMemberId: Int): Funeral? {
        return clientRepository.getProgressingFuneralByIdDirectorMemberId(directorMemberId)
    }

    fun getCellphoneNoFormatted(id: Int): String {
        return clientRepository.getCellphoneNoFormatted(id)
    }

    fun getFamilyByClientId(clientId: Int): Family {
        return clientRepository.getFamilyByClientId(clientId)
    }

    fun getFuneralsByDirectorMemberIdAndProgress(directorMemberId: Int, progress: Boolean): List<Funeral> {
        return clientRepository.getFuneralsByDirectorMemberIdAndProgress(directorMemberId, progress)
    }

    fun addFamily(familyRelation: String, familyName: String, familyCellphoneNo: String, clientId: Int): ResultData {
        if(familyRelation.isBlank()){
            return ResultData.from("F-1", "입력한 유가족 관계가 올바른지 확인해주십시오.")
        }
        if(familyName.isBlank()){
            return ResultData.from("F-2", "유가족의 이름이 올바른지 확인해주십시오.")
        }
        if(familyCellphoneNo.isBlank()){
            return ResultData.from("F-3", "유가족의 핸드폰번호가 올바른지 확인해주십시오.")
        }
        // 넘겨받을 clientId가 존재하지 않는 것은, 잘못된 접근
        if(clientId == 0){
            return ResultData.from("F-4", "잘못된 접근입니다.")
        }
        // 해당 clientId로 조회된 데이터 중, 중복되는 데이터가 있는지 확인
        val existingFamily = clientRepository.getFamilyByClientIdAndAll(familyRelation, familyName, familyCellphoneNo, clientId)
        if(existingFamily != null){
            return ResultData.from("F-5", "이미 동일한 유가족 또는 상주 정보를 입력하셨습니다.")
        }

        // 예외를 모두 통과한 경우
        clientRepository.addFamily(familyRelation, familyName, familyCellphoneNo, clientId)

        return ResultData.from("S-1", "유가족 정보를 추가했습니다.")
    }

    fun getFamilyMembersByClientId(clientId: Int): List<Family> {
        return clientRepository.getFamilyMembersByClientId(clientId)
    }

    fun removeFamily(familyRelation: String, familyName: String, familyCellphoneNo: String, clientId: Int): ResultData {

        val familyMember = clientRepository.getFamilyMemberByAll(familyRelation, familyName, familyCellphoneNo, clientId)
        if(familyMember == null){
            return ResultData.from("F-1", "담당자에게 문의해주십시오.(010-4921-9810)")
        }

        clientRepository.removeFamily(familyRelation, familyName, familyCellphoneNo, clientId)
        return ResultData.from("S-1", "해당 유가족의 정보를 삭제했습니다.")
    }

    fun modifyFuneral(funeralHallName: String, funeralHallRoom: String, deceasedName: String, frontNum: String, backNum: String, deceasedHomeAddress: String, familyClan: String, religion: String, birth: String, deceasedDate: String, lunar: Int, funeralMethod: Int, cremationLocation: String, buryLocation: String, cause: String, papers: Int, autopsyCheck: Boolean, casketDate: String, casketTime: String, leavingTime: String, leavingDate: String, chiefName: String, chiefRelation: String, chiefCellphoneNo: String, chiefAddress: String, clientId: Int): ResultData {
L
        clientRepository.modifyClient(funeralHallName, funeralHallRoom)
    }


}
