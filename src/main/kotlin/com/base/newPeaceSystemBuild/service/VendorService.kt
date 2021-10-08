package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.ClientRepository
import com.base.newPeaceSystemBuild.repository.MemberRepository
import com.base.newPeaceSystemBuild.repository.VendorRepository
import com.base.newPeaceSystemBuild.vo.ResultData
import com.base.newPeaceSystemBuild.vo.Rq
import com.base.newPeaceSystemBuild.vo.client.Funeral
import com.base.newPeaceSystemBuild.vo.standard.*
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

    fun getCoffinTransporters(): List<CoffinTransporter> {
        return vendorRepository.getCoffinTransporters()
    }

    fun getFlowers(): List<Flower> {
        // 취급할 제단꽃 standard를 불러온다.
        return vendorRepository.getFlowers()
    }

    fun getFuneralById(funeralId: Int): Funeral? {
        return vendorRepository.getFuneralById(funeralId)
    }

    fun modifyFuneralIntoCoffinTransporterUseStatus(funeralId: Int, deceasedHomeAddress: String): ResultData {
        val funeral = getFuneralById(funeralId) ?: return ResultData.from("F-1", "올바르지 않은 접근입니다.")

        val roleCategoryId = 3

        val detail = "coffinTransporter"
        // order 정보 조회
        val order = getOrderByFuneralIdAndCompletionStatusAndDetail(
            funeral.id,
            false,
            detail
        )

        // order 정보 유무에따라 로직에 차별성을 줌
        // DB에 order 정보가 없을경우엔 Insert 문으로 Order 를 추가
        if (order == null) {
            vendorRepository.insertIntoOrder(
                funeral.id,
                rq.getLoginedMember()!!.id,
                roleCategoryId,
                1,
                detail
            )

            val orderId = vendorRepository.getLastInsertId()

            // 상세주문 테이블도 Insert 해줌
            when (detail) {
                "coffinTransporter" -> {
                    vendorRepository.insertIntoCoffinTransporterOrder(orderId, deceasedHomeAddress)
                }
            }
        }
        // DB에 order 정보가 있을경우엔 Update 문으로 Order 를 수정
        else {
            // 운구차량은 수정시에도 Order 테이블에는 변화가 없음.
            // 반복문으로 돌아가는 detail 에 따라 상세주문 테이블도 Update 해줌
            when (detail) {
                "coffinTransporter" -> {
                    vendorRepository.modifyCoffinTransporterOrderIntoDeceasedHomeAddressByOrderId(
                        order.id,
                        deceasedHomeAddress
                    )
                }
            }
        }

        // funeral 테이블에 CoffinTransporterUseStatus 를 업데이트 한다.
        vendorRepository.modifyFuneralIntoCoffinTransporterUseStatus(funeralId, true)
        // 연결된 물품 공급업자에게 주문 정보를 주기 위해 orderId를 성공 시, 같이 return

        return ResultData.from("S-1", "운구차 출동신청을 보냈습니다.", "funeral", funeral)
    }

    fun modifyFuneralIntoFlowerId(
        funeralId: Int,
        flowerId: Int,
        flowerTributeId: Int,
        bunchCnt: Int,
        packing: Char
    ): ResultData {
        val funeral = getFuneralById(funeralId) ?: return ResultData.from("F-1", "올바르지 않은 접근입니다.")

        var packingBool = false

        if (packing == 'Y') {
            packingBool = true
        }

        //  order에 대한 데이터를 DB에 저장
        val roleCategoryId = 1

        // detail List
        val details = mutableListOf<String>()
        details.add("flower")
        details.add("flowerTribute")

        for(detail in details){
            // detail 에 따라 standardId 값을 변경
            val standardId = when (detail) {
                "flower" -> {
                    flowerId
                }
                "flowerTribute" -> {
                    flowerTributeId
                }
                else -> {
                    0
                }
            }

            // order 정보 조회
            val order = getOrderByFuneralIdAndCompletionStatusAndDetail(
                funeral.id,
                false,
                detail
            )
            // order 정보 유무에따라 로직에 차별성을 줌
            // DB에 order 정보가 없을경우엔 Insert 문으로 Order 를 추가
            if (order == null) {
                vendorRepository.insertIntoOrder(
                    funeral.id,
                    rq.getLoginedMember()!!.id,
                    roleCategoryId,
                    standardId,
                    detail
                )

                val orderId = vendorRepository.getLastInsertId()

                // 반복문으로 돌아가는 detail 에 따라 상세주문 테이블도 Insert 해줌
                when (detail) {
                    "flower" -> {
                        vendorRepository.insertIntoFlowerOrder(orderId)
                    }
                    "flowerTribute" -> {
                        vendorRepository.insertIntoFlowerTributeOrder(orderId, bunchCnt, packingBool)
                    }
                }
            }
            // DB에 order 정보가 있을경우엔 Update 문으로 Order 를 수정
            else {
                vendorRepository.modifyOrderIntoStandardIdByFuneralIdDirectorMemberIdRoleCategoryIdDetailCompletionStatus(
                    standardId,
                    funeral.id,
                    rq.getLoginedMember()!!.id,
                    roleCategoryId,
                    detail,
                    false
                )
                // 반복문으로 돌아가는 detail 에 따라 상세주문 테이블도 Update 해줌
                when (detail) {
                    "flowerTribute" -> {
                        vendorRepository.modifyFlowerTributeOrderIntoBunchCntAndPackingByOrderId(
                            bunchCnt,
                            packingBool,
                            order.id
                        )
                    }
                }
            }
        }

        // funeral 테이블에 flowerId 를 업데이트 한다.
        vendorRepository.modifyFuneralIntoFlowerId(funeralId, flowerId)
        vendorRepository.modifyFuneralIntoFlowerTributeId(funeralId, flowerTributeId)
        // 연결된 물품 공급업자에게 주문 정보를 주기 위해 orderId를 성공 시, 같이 return

        return ResultData.from("S-1", "제단꽃 및 헌화에 주문 정보를 입력했습니다.", "funeral", funeral)

        // return ResultData.from("S-2", "제단꽃 및 헌화에 주문 정보를 수정했습니다.", "funeral", funeral)
    }

    fun modifyFuneralIntoMourningClothId(
        funeralId: Int,
        femaleMourningClothId: Int,
        femaleClothCnt: Int,
        femaleClothColor: String,
        maleMourningClothId: Int,
        maleClothCnt: Int,
        shirtId: Int,
        shirtCnt: Int,
        necktieId: Int,
        necktieCnt: Int
    ): ResultData {
        val funeral = getFuneralById(funeralId) ?: return ResultData.from("F-1", "올바르지 않은 접근입니다.")

        //  order에 대한 데이터를 DB에 저장
        val roleCategoryId = 2

        // detail List
        val details = mutableListOf<String>()
        details.add("femaleMourningCloth")
        details.add("maleMourningCloth")
        details.add("shirt")
        details.add("necktie")

        for(detail in details){
            // detail 에 따라 standardId 값을 변경
            val standardId = when (detail) {
                "femaleMourningCloth" -> {
                    femaleMourningClothId
                }
                "maleMourningCloth" -> {
                    maleMourningClothId
                }
                "shirt" -> {
                    shirtId
                }
                "necktie" -> {
                    necktieId
                }
                else -> {
                    0
                }
            }

            // order 정보 조회
            val order = getOrderByFuneralIdAndCompletionStatusAndDetail(
                funeral.id,
                false,
                detail
            )
            // order 정보 유무에따라 로직에 차별성을 줌
            // DB에 order 정보가 없을경우엔 Insert 문으로 Order 를 추가
            if (order == null) {
                vendorRepository.insertIntoOrder(
                    funeral.id,
                    rq.getLoginedMember()!!.id,
                    roleCategoryId,
                    standardId,
                    detail
                )

                val orderId = vendorRepository.getLastInsertId()

                // 반복문으로 돌아가는 detail 에 따라 상세주문 테이블도 Insert 해줌
                when (detail) {
                    "femaleMourningCloth" -> {
                        vendorRepository.insertIntoFemaleMourningClothOrder(orderId, femaleClothCnt, femaleClothColor)
                    }
                    "maleMourningCloth" -> {
                        vendorRepository.insertIntoMaleMourningClothOrder(orderId, maleClothCnt)
                    }
                    "shirt" -> {
                        vendorRepository.insertIntoShirtOrder(orderId, shirtCnt)
                    }
                    "necktie" -> {
                        vendorRepository.insertIntoNecktieOrder(orderId, necktieCnt)
                    }
                }
            }
            // DB에 order 정보가 있을경우엔 Update 문으로 Order 를 수정
            else {
                vendorRepository.modifyOrderIntoStandardIdByFuneralIdDirectorMemberIdRoleCategoryIdDetailCompletionStatus(
                    standardId,
                    funeral.id,
                    rq.getLoginedMember()!!.id,
                    roleCategoryId,
                    detail,
                    false
                )
                // 반복문으로 돌아가는 detail 에 따라 상세주문 테이블도 Update 해줌
                when (detail) {
                    "femaleMourningCloth" -> {
                        vendorRepository.modifyFemaleMourningClothOrderIntoFemaleClothCntAndFemaleClothColorByOrderId(
                            femaleClothCnt,
                            femaleClothColor,
                            order.id
                        )
                    }
                    "maleMourningCloth" -> {
                        vendorRepository.modifyMaleMourningClothOrderIntoMaleClothCntByOrderId(
                            maleClothCnt,
                            order.id
                        )
                    }
                    "shirt" -> {
                        vendorRepository.modifyShirtOrderIntoShirtCntByOrderId(
                            shirtCnt,
                            order.id
                        )
                    }
                    "necktie" -> {
                        vendorRepository.modifyNecktieOrderIntoNecktieCntByOrderId(
                            necktieCnt,
                            order.id
                        )
                    }
                }
            }
        }

        // funeral 테이블에 standardId 들을 업데이트 한다.
        vendorRepository.modifyFuneralIntoFemaleMourningClothId(funeralId, femaleMourningClothId)
        vendorRepository.modifyFuneralIntoMaleMourningClothId(funeralId, maleMourningClothId)
        vendorRepository.modifyFuneralIntoShirtId(funeralId, shirtId)
        vendorRepository.modifyFuneralIntoNecktieId(funeralId, necktieId)

        // 연결된 물품 공급업자에게 주문 정보를 주기 위해 orderId를 성공 시, 같이 return

        return ResultData.from("S-1", "상복 주문 정보를 입력했습니다.", "funeral", funeral)
    }

    fun getFlowerById(flowerId: Int): Flower? {
        return vendorRepository.getFlowerById(flowerId)
    }

    fun modifyOrderIntoVendorMemberIdAndOrderStatusByDirectorMemberIdAndDetail(
        memberId: Int,
        funeralId: Int,
        detail: String
    ): ResultData {
        val funeral = getFuneralById(funeralId) ?: return ResultData.from("F-1", "장례 정보가 조회되지 않습니다.")

        // 해당 페이지에 들어올 수 있으면, 이미 vendor 로 등록된 상태이기때문에 vendor 등록시 선택된 extra__roleCategoryId를 가져온다. 어떤 물품의 공급자인지에 관한 칼럼이다.
        val roleCategoryId = rq.getLoginedMember()!!.extra__roleCategoryId!!

        val order = vendorRepository.getOrderByFuneralIdAndRoleCategoryIdAndDetail(funeralId, roleCategoryId, detail)
            ?: return ResultData.from("F-2", "주문정보가 조회되지않습니다.")

        if (order.orderStatus) {
            // order 테이블에 vendorMemberId 칼럼값이 현재 로그인한 회원의 ID값이랑 같으면 로그인한 회원이 해당 주문을 받은것
            if (order.vendorMemberId == memberId) {
                return ResultData.from("F-3", "이미 수락하셨습니다. '주문내역 확인' 에서 확인해주세요.")
            }
            // order 테이블에 vendorMemberId 칼럼은 default 값이 0이기때문에 0이면 아무도 주문을 받지않은상태다. 반대로 0이 아니란것은 누군가 주문을 받은상태
            if (order.vendorMemberId != 0) {
                return ResultData.from("F-4", "다른 업체에서 먼저 주문을 받았습니다.")
            }
        }
        // 장례지도사가 상품을 주문하면 order 테이블에 데이터가 추가된다. 이때 vendorMemberId는 기본값 0인상태, orderStatus = false 인 상태이다.
        // 사업자가 주문을 받으면 테이블의 데이터를 수정한다 vendorMemberId 는 로그인 정보의 ID로 바꿔주고 orderStatus 는 true 로 바꿔준다.
        // 바꿔줄 데이터는 where 절을 통해 directorMemberId 와 roleCategoryId가 일치하는 데이터의 정보만 바꿔준다.
        vendorRepository.modifyOrderIntoVendorMemberIdAndOrderStatusByDirectorMemberIdAndRoleCategoryId(
            memberId,
            funeral.directorMemberId,
            roleCategoryId,
            true
        )

        return ResultData.from("S-1", "주문접수가 완료되었습니다.", "funeral", funeral)
    }

    fun getOrdersByVendorMemberIdAndOrderStatus(
        vendorMemberId: Int,
        roleCategoryId: Int,
        orderStatus: Boolean,
        completionStatus: Boolean,
        detail: String
    ): List<Order> {
        return vendorRepository.getOrdersByVendorMemberIdAndOrderStatus(
            vendorMemberId,
            roleCategoryId,
            orderStatus,
            completionStatus,
            detail
        )
    }

    fun modifyOrderIntoCompleteStatusByVendorMemberIdAndClientId(
        vendorMemberId: Int,
        funeralId: Int,
        completionStatus: Boolean
    ): ResultData {
        vendorRepository.modifyOrderIntoCompleteStatusByVendorMemberIdAndFuneralId(
            vendorMemberId,
            funeralId,
            completionStatus
        )

        return ResultData.from("S-1", "완료")
    }

    fun getFlowerTributes(): List<FlowerTribute> {
        return vendorRepository.getFlowerTributes()
    }

    fun getFlowerTributeById(flowerTributeId: Int): FlowerTribute? {
        return vendorRepository.getFlowerTributeById(flowerTributeId)
    }

    fun getOrderByDirectorMemberIdAndCompletionStatusAndDetail(directorMemberId: Int, completionStatus: Boolean, detail: String): Order? {
        return vendorRepository.getOrderByDirectorMemberIdAndCompletionStatusAndDetail(directorMemberId, completionStatus, detail)
    }

    fun getOrderByFuneralIdAndCompletionStatusAndDetail(
        funeralId: Int,
        completionStatus: Boolean,
        detail: String
    ): Order? {
        return vendorRepository.getOrderByFuneralIdAndCompletionStatusAndDetail(
            funeralId,
            completionStatus,
            detail
        )
    }

    fun getFemaleMourningCloths(): List<MourningCloth> {
        return vendorRepository.getFemaleMourningCloths()
    }

    fun getMaleMourningCloths(): List<MourningCloth> {
        return vendorRepository.getMaleMourningCloths()
    }

    fun getShirts(): List<MourningCloth> {
        return vendorRepository.getShirts()
    }

    fun getNeckties(): List<MourningCloth> {
        return vendorRepository.getNeckties()
    }

    fun getFemaleMourningClothById(femaleMourningClothId: Int): MourningCloth? {
        return vendorRepository.getFemaleMourningClothById(femaleMourningClothId)
    }

    fun getMaleMourningClothById(maleMourningClothId: Int): MourningCloth? {
        return vendorRepository.getMaleMourningClothById(maleMourningClothId)
    }

    fun getShirtById(shirtId: Int): MourningCloth? {
        return vendorRepository.getShirtById(shirtId)
    }
    fun getNecktieById(necktieId: Int): MourningCloth? {
        return vendorRepository.getNecktieById(necktieId)
    }

    fun getShroudById(shroudId: Int): Shroud? {
        return vendorRepository.getShroudById(shroudId)
    }

    fun getShrouds(): List<Shroud> {
        return vendorRepository.getShrouds()
    }


}
