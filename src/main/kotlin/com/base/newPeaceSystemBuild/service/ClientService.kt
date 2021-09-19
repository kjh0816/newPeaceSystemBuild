package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.ClientRepository
import com.base.newPeaceSystemBuild.repository.MemberRepository
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.Aligo__send__ResponseBody
import com.base.newPeaceSystemBuild.vo.ResultData
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.client.Client
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
        clientRepository.insertIntoClient(memberId, deceasedName, relatedName, cellphoneNo, location, address)
        val clientId = clientRepository.getLastInsertId()

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
        //
        // 영업자와 장례지도사에게 장례지도사가 배정된 것에 대한 정보를 문자로 알림(끝)

        return ResultData.from("S-1", "출동 요청을 승낙하였습니다.", "client", client)
    }

    fun getProgressingFuneralByIdDirectorMemberId(directorMemberId: Int): Funeral? {
        return clientRepository.getProgressingFuneralByIdDirectorMemberId(directorMemberId)
    }

    fun getCellphoneNoFormatted(id: Int): String {
        return clientRepository.getCellphoneNoFormatted(id)
    }
}
