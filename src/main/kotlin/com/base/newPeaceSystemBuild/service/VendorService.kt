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
import com.base.newPeaceSystemBuild.vo.standard.FlowerTribute
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

    fun modifyFuneralIntoFlowerId(funeralId:Int, flowerId: Int, flowerTributeId: Int, bunchCnt: Int, packing: Char): ResultData {
        val funeral = getFuneralById(funeralId) ?: return ResultData.from("F-1", "올바르지 않은 접근입니다.")

        if(funeral.flowerId != 0){
            return ResultData.from("F-3", "이미 제단꽃을 신청하였습니다.")
        }

        if(funeral.flowerTributeId != 0){
            return ResultData.from("F-3", "이미 헌화를 신청하였습니다.")
        }

        val client = clientRepository.getClientById(funeral.clientId) ?: return ResultData.from("F-1", "올바르지 않은 접근입니다.")

        //  order에 대한 데이터를 DB에 저장
        val roleCategoryId = 1

        var detail = ""

        if(flowerId != 0){
            detail = "flower"
            vendorRepository.insertIntoOrder(client.id, rq.getLoginedMember()!!.id, roleCategoryId, flowerId, detail)

            val flowerOrderId = vendorRepository.getLastInsertId()
            vendorRepository.insertIntoFlowerOrder(flowerOrderId)
        }

        if(flowerTributeId != 0){
            detail = "flowerTribute"
            vendorRepository.insertIntoOrder(client.id, rq.getLoginedMember()!!.id, roleCategoryId, flowerTributeId, detail)

            var packingBool = false

            if(packing == 'Y'){
                packingBool = true
            }

            val flowerTributeOrderId = vendorRepository.getLastInsertId()
            vendorRepository.insertIntoFlowerTributeOrder(flowerTributeOrderId, bunchCnt, packingBool)
        }


        // funeral 테이블에 flowerId 를 업데이트 한다.
        vendorRepository.modifyFuneralIntoFlowerId(funeralId, flowerId)
        vendorRepository.modifyFuneralIntoFlowerTributeId(funeralId, flowerTributeId)
        // 연결된 물품 공급업자에게 주문 정보를 주기 위해 orderId를 성공 시, 같이 return

        return ResultData.from("S-1", "제단꽃 및 헌화에 주문 정보를 입력했습니다.", "funeral", funeral)

        // return ResultData.from("S-2", "제단꽃 및 헌화에 주문 정보를 수정했습니다.", "funeral", funeral)
    }

    fun getFlowerById(flowerId: Int): Flower? {
        return vendorRepository.getFlowerById(flowerId)
    }

    fun modifyOrderIntoVendorMemberIdAndOrderStatusByDirectorMemberIdAndDetail(memberId: Int, clientId: Int, detail: String): ResultData{
        val client = clientRepository.getClientById(clientId) ?: return ResultData.from("F-1", "고인의 정보가 조회되지않습니다.")

        // 해당 페이지에 들어올 수 있으면, 이미 vendor 로 등록된 상태이기때문에 vendor 등록시 선택된 extra__roleCategoryId를 가져온다. 어떤 물품의 공급자인지에 관한 칼럼이다.
        val roleCategoryId = rq.getLoginedMember()!!.extra__roleCategoryId!!

        val order = vendorRepository.getOrderByClientIdAndRoleCategoryIdAndDetail(clientId, roleCategoryId, detail) ?: return ResultData.from("F-2", "주문정보가 조회되지않습니다.")

        if(order.orderStatus){
            // order 테이블에 vendorMemberId 칼럼값이 현재 로그인한 회원의 ID값이랑 같으면 로그인한 회원이 해당 주문을 받은것
            if(order.vendorMemberId == memberId){
                return ResultData.from("F-3", "이미 수락하셨습니다. '주문내역 확인' 에서 확인해주세요.")
            }
            // order 테이블에 vendorMemberId 칼럼은 default 값이 0이기때문에 0이면 아무도 주문을 받지않은상태다. 반대로 0이 아니란것은 누군가 주문을 받은상태
            if(order.vendorMemberId != 0){
                return ResultData.from("F-4", "다른 업체에서 먼저 주문을 받았습니다.")
            }
        }
        // 장례지도사가 상품을 주문하면 order 테이블에 데이터가 추가된다. 이때 vendorMemberId는 기본값 0인상태, orderStatus = false 인 상태이다.
        // 사업자가 주문을 받으면 테이블의 데이터를 수정한다 vendorMemberId 는 로그인 정보의 ID로 바꿔주고 orderStatus 는 true 로 바꿔준다.
        // 바꿔줄 데이터는 where 절을 통해 directorMemberId 와 roleCategoryId가 일치하는 데이터의 정보만 바꿔준다.
        vendorRepository.modifyOrderIntoVendorMemberIdAndOrderStatusByDirectorMemberIdAndRoleCategoryId(memberId, client.directorMemberId, roleCategoryId, true)

        return ResultData.from("S-1", "주문접수가 완료되었습니다.", "client", client)
    }

    fun getOrdersByVendorMemberIdAndOrderStatus(vendorMemberId: Int, roleCategoryId: Int, orderStatus: Boolean, completionStatus: Boolean, detail: String): List<Order> {
        return vendorRepository.getOrdersByVendorMemberIdAndOrderStatus(vendorMemberId, roleCategoryId, orderStatus, completionStatus, detail)
    }

    fun modifyOrderIntoCompleteStatusByVendorMemberIdAndClientId(vendorMemberId: Int, clientId: Int, completionStatus: Boolean): ResultData {
        vendorRepository.modifyOrderIntoCompleteStatusByVendorMemberIdAndClientId(vendorMemberId, clientId, completionStatus)

        return ResultData.from("S-1", "완료")
    }

    fun getFlowerTributes(): List<FlowerTribute> {
        return vendorRepository.getFlowerTributes()
    }

    fun getFlowerTributeById(flowerTributeId: Int): FlowerTribute? {
        return vendorRepository.getFlowerTributeById(flowerTributeId)
    }

    fun getFlowerTributeOrderByDirectorMemberId(directorMemberId: Int): Order? {
        return vendorRepository.getFlowerTributeOrderByDirectorMemberId(directorMemberId)
    }

}