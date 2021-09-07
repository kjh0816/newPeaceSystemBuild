package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.ClientRepository
import com.base.newPeaceSystemBuild.repository.MemberRepository
import com.base.newPeaceSystemBuild.repository.VendorRepository
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.Aligo__send__ResponseBody
import com.base.newPeaceSystemBuild.vo.ResultData
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.client.Funeral
import com.base.newPeaceSystemBuild.vo.member.Member
import com.base.newPeaceSystemBuild.vo.standard.Flower
import com.base.newPeaceSystemBuild.vo.vendor.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class VendorService(
    private val vendorRepository: VendorRepository,
    private val memberRepository: MemberRepository,
    private val clientRepository: ClientRepository
) {
    @Autowired
    private lateinit var rq: Rq;

    fun getFlowers(): List<Flower> {
        // 취급할 제단꽃 standard를 불러온다.
        return vendorRepository.getFlowers()
    }

    fun getFuneralById(funeralId: Int): Funeral?{
        return vendorRepository.getFuneralById(funeralId)
    }

    fun modifyFuneralIntoFlowerId(funeralId:Int, flowerId: Int): ResultData {
        val funeral = getFuneralById(funeralId) ?: return ResultData.from("F-1", "올바르지 않은 접근입니다.")

        if(funeral.flowerId != 0){
            return ResultData.from("F-3", "이미 제단꽃을 신청하였습니다.")
        }

        val client = clientRepository.getClientById(funeral.clientId) ?: return ResultData.from("F-1", "올바르지 않은 접근입니다.")

        //  order에 대한 데이터를 DB에 저장
        val roleCategoryId = 1

        vendorRepository.insertIntoOrder(client.id, rq.getLoginedMember()!!.id, roleCategoryId, flowerId)

        val orderId = vendorRepository.getLastInsertId()

        // 문자 메세지 전달 (시작)
        // client 정보 중, location을 반영해서 해당하는 물품 등록업자들에 문자 메세지 전달


        // 직업: 물품 등록업자 ( roleLevel = 4 )
        val roleLevel = 4

        // 직업을 구분하기 위해 roleLevel 지역을 구분하기 위해 location을 매개변수로 받아, members를 출력
        // 추후 범용적으로 이 함수를 사용하기 위해 roleCategoryId를 넣었다. (0일 경우, roleCategoryId가 내부적으로 적용되지 않는다.)
        // 반대로, vendor의 경우, roleCategoryId 값을 넣으면 내부적으로 적용된다.
        val vendors: List<Member> = memberRepository.getMembersByLocationAndRole(client.location, roleLevel, roleCategoryId)

        // 몇 명의 등록업자가 조회되었고, 문자가 갈 것인지를 알려주기 위한 변수
        val directorsCount = vendors.size
        // location으로 조회했을 때, 해당 지역에 한 명도 없는 경우에 대한 예외처리
        if(vendors.isEmpty()){
            return ResultData.from("F-2", "${client.location}에 등록된 제단꽃 업체가 없습니다.")
        }

        // 발신자 전화번호
        val from = "01049219810"
        // 1명 이상의 수신자 전화번호 ( 알리고 API에서 수신인(receiver)로써 인식 가능한 상태로 넣어주는 함수 )
        // 다른 직업에 대해서도 재사용 가능
        val to = Ut.getCellphoneNosFromMembers(vendors)
        // 문자 내용
        val msg = "ㅎㅇ"

        // 알리고 API에서 문자 전송에 필요한 데이터를 넘겨주고, 알리고로부터 반환된 결과값 rb
        val rb: Aligo__send__ResponseBody = Ut.sendSms(from, to.toString(), msg, true)

        // 문자 메세지 전달 (끝)

        // funeral 테이블에 flowerId 를 업데이트 한다.
        vendorRepository.modifyFuneralIntoFlowerId(funeralId, flowerId)
        // 연결된 물품 공급업자에게 주문 정보를 주기 위해 orderId를 성공 시, 같이 return

        return ResultData.from("S-1", "${directorsCount}곳의 업체에게 제작 요청했습니다.", "from", from, "to", to, "msg", msg, "rb", rb, "orderId", orderId, "funeral", funeral)
    }

    fun getFlowerById(flowerId: Int): Flower {
        return vendorRepository.getFlowerById(flowerId)
    }

    fun modifyOrderIntoDirectorMemberIdByDirectorId(memberId: Int, clientId: Int): ResultData{
        val client = clientRepository.getClientById(clientId) ?: return ResultData.from("F-1", "고인의 정보가 조회되지않습니다.")

        val order = vendorRepository.getOrderByClientId(clientId)

        if(order == null){
            return ResultData.from("F-2", "주문정보가 조회되지않습니다.")
        }

        if(order.orderStatus){
            if(order.vendorMemberId == memberId){
                return ResultData.from("F-3", "이미 수락하셨습니다. '내가받은 주문정보 보기' 에서 확인해주세요.")
            }

            if(order.vendorMemberId != 0){
                return ResultData.from("F-4", "다른 업체에서 먼저 주문을 받았습니다.")
            }
        }

        vendorRepository.modifyOrderIntoVendorMemberIdAndOrderStatusByDirectorMemberId(memberId, client.directorMemberId, true)

        return ResultData.from("S-1", "주문접수가 완료되었습니다.", "client", client)
    }

    fun getOrdersByVendorMemberIdAndOrderStatus(vendorMemberId: Int, orderStatus: Boolean, completionStatus: Boolean): List<Order> {
        return vendorRepository.getOrdersByVendorMemberIdAndOrderStatus(vendorMemberId, orderStatus, completionStatus)
    }

    fun modifyOrderIntoCompleteStatusByVendorMemberIdAndClientId(vendorMemberId: Int, clientId: Int, completionStatus: Boolean): ResultData {
        vendorRepository.modifyOrderIntoCompleteStatusByVendorMemberIdAndClientId(vendorMemberId, clientId, completionStatus)

        return ResultData.from("S-1", "완료")
    }
}