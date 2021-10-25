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
import com.base.newPeaceSystemBuild.vo.standard.*
import com.base.newPeaceSystemBuild.vo.vendor.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class VendorService(
    private val vendorRepository: VendorRepository,
    private val memberRepository: MemberRepository,
    private val clientRepository: ClientRepository,
    private val clientService: ClientService,
    private val memberRoleService: MemberRoleService

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
                vendorRepository.modifyOrderIntoStandardIdByFuneralIdRoleCategoryIdDetailCompletionStatus(
                    standardId,
                    funeral.id,
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

    fun modifyFuneralIntoShroudId(
        funeralId: Int,
        shroudId: Int,
        coffinId: Int
    ): ResultData {
        val funeral = getFuneralById(funeralId) ?: return ResultData.from("F-1", "올바르지 않은 접근입니다.")

        if(coffinId == 0){
            return ResultData.from("F-2", "관을 선택해주십시오.")
        }



        // detail List
        val details = mutableListOf<String>()
        details.add("shroud")
        details.add("coffin")

        for(detail in details){
            // detail 에 따라 standardId 값을 변경
            val standardId = when (detail) {
                "shroud" -> {
                    shroudId
                }
                "coffin" -> {
                    coffinId
                }
                else -> {
                    0
                }
            }

            //  order에 대한 데이터를 DB에 저장
            val roleCategoryId = when (detail){
                "shroud" -> {
                    4
                }
                "coffin" -> {
                    5
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
            // order 정보 유무에 따라 로직에 차별성을 줌
            // DB에 order 정보가 없을경우엔 Insert 문으로 Order 를 추가
            if (order == null) {
                vendorRepository.insertIntoOrder(
                    funeral.id,
                    roleCategoryId,
                    standardId,
                    detail
                )

                val orderId = vendorRepository.getLastInsertId()

                // 반복문으로 돌아가는 detail 에 따라 상세주문 테이블도 Insert 해줌
                when (detail) {
                    "shroud" -> {
                        vendorRepository.insertIntoShroudOrder(orderId)
                    }
                    "coffin" -> {
                        vendorRepository.insertIntoCoffinOrder(orderId)
                    }
                }
            }
            // DB에 order 정보가 있을경우엔 Update 문으로 Order 를 수정
            else {
                vendorRepository.modifyOrderIntoStandardIdByFuneralIdRoleCategoryIdDetailCompletionStatus(
                    standardId,
                    funeral.id,
                    roleCategoryId,
                    detail,
                    false
                )
                // 반복문으로 돌아가는 detail 에 따라 상세주문 테이블도 Update 해줌
                when (detail) {
                    "shroud" -> {

                    }
                    "coffin" -> {

                    }
                }
            }
        }


        // funeral 테이블에 shroudId 를 업데이트 한다.
        vendorRepository.modifyFuneralIntoShroudId(funeralId, shroudId)
        vendorRepository.modifyFuneralIntoCoffinId(funeralId, coffinId)
        // 연결된 물품 공급업자에게 주문 정보를 주기 위해 orderId를 성공 시, 같이 return

        return ResultData.from("S-1", "수의 및 관 주문 정보를 입력했습니다.", "funeral", funeral)
    }

    fun modifyFuneralIntoMourningClothId(
        funeralId: Int,
        femaleMourningClothBlackId: Int,
        femaleMourningClothBlackCnt: Int,
        femaleMourningClothWhiteId: Int,
        femaleMourningClothWhiteCnt: Int,
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
        details.add("femaleMourningClothBlack")
        details.add("femaleMourningClothWhite")
        details.add("maleMourningCloth")
        details.add("shirt")
        details.add("necktie")

        for(detail in details){
            // detail 에 따라 standardId 값을 변경
            val standardId = when (detail) {
                "femaleMourningClothBlack" -> {
                    femaleMourningClothBlackId
                }
                "femaleMourningClothWhite" -> {
                    femaleMourningClothWhiteId
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
                    roleCategoryId,
                    standardId,
                    detail
                )

                val orderId = vendorRepository.getLastInsertId()

                // 반복문으로 돌아가는 detail 에 따라 상세주문 테이블도 Insert 해줌
                when (detail) {
                    "femaleMourningClothBlack" -> {
                        vendorRepository.insertIntoFemaleMourningClothBlackOrder(orderId, femaleMourningClothBlackCnt)
                    }
                    "femaleMourningClothWhite" -> {
                        vendorRepository.insertIntoFemaleMourningClothWhiteOrder(orderId, femaleMourningClothWhiteCnt)
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
                vendorRepository.modifyOrderIntoStandardIdByFuneralIdRoleCategoryIdDetailCompletionStatus(
                    standardId,
                    funeral.id,
                    roleCategoryId,
                    detail,
                    false
                )
                // 반복문으로 돌아가는 detail 에 따라 상세주문 테이블도 Update 해줌
                when (detail) {
                    "femaleMourningClothBlack" -> {
                        vendorRepository.modifyFemaleMourningClothBlackOrderIntoFemaleClothCntAndFemaleClothColorByOrderId(
                            femaleMourningClothBlackCnt,
                            order.id
                        )
                    }
                    "femaleMourningClothWhite" -> {
                        vendorRepository.modifyFemaleMourningClothWhiteOrderIntoFemaleClothCntAndFemaleClothColorByOrderId(
                            femaleMourningClothWhiteCnt,
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
        vendorRepository.modifyFuneralIntoFemaleMourningClothBlackId(funeralId, femaleMourningClothBlackId)
        vendorRepository.modifyFuneralIntoFemaleMourningClothWhiteId(funeralId, femaleMourningClothWhiteId)
        vendorRepository.modifyFuneralIntoMaleMourningClothId(funeralId, maleMourningClothId)
        vendorRepository.modifyFuneralIntoShirtId(funeralId, shirtId)
        vendorRepository.modifyFuneralIntoNecktieId(funeralId, necktieId)

        // 연결된 물품 공급업자에게 주문 정보를 주기 위해 orderId를 성공 시, 같이 return

        return ResultData.from("S-1", "상복 주문 정보를 입력했습니다.", "funeral", funeral)
    }

    fun modifyFuneralIntoMortuaryIds(
        funeralId: Int,
        incenseId: Int,
        incenseCnt: Int
    ): ResultData {
        val funeral = getFuneralById(funeralId) ?: return ResultData.from("F-1", "올바르지 않은 접근입니다.")

        //  order에 대한 데이터를 DB에 저장
        val roleCategoryId = 0

        // detail List
        val details = mutableListOf<String>()
        details.add("incense")

        for(detail in details){
            // detail 에 따라 standardId 값을 변경
            val standardId = when (detail) {
                "incense" -> {
                    incenseId
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
                    roleCategoryId,
                    standardId,
                    detail
                )

                val orderId = vendorRepository.getLastInsertId()

                // 반복문으로 돌아가는 detail 에 따라 상세주문 테이블도 Insert 해줌
                when (detail) {
                    "incense" -> {
                        vendorRepository.insertIntoIncenseOrder(orderId, incenseCnt)
                    }
                }
            }
            // DB에 order 정보가 있을경우엔 Update 문으로 Order 를 수정
            else {
                vendorRepository.modifyOrderIntoStandardIdByFuneralIdRoleCategoryIdDetailCompletionStatus(
                    standardId,
                    funeral.id,
                    roleCategoryId,
                    detail,
                    false
                )
                // 반복문으로 돌아가는 detail 에 따라 상세주문 테이블도 Update 해줌
                when (detail) {
                    "incense" -> {
                        vendorRepository.modifyIncenseOrderIntoFemaleClothCntAndFemaleClothColorByOrderId(
                            incenseCnt,
                            order.id
                        )
                    }
                }
            }
        }

        // funeral 테이블에 standardId 들을 업데이트 한다.
        vendorRepository.modifyFuneralIntoIncenseId(funeralId, incenseId)

        // 연결된 물품 공급업자에게 주문 정보를 주기 위해 orderId를 성공 시, 같이 return

        return ResultData.from("S-1", "빈소용품 주문 정보를 입력했습니다.", "funeral", funeral)
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
        vendorRepository.modifyOrderIntoVendorMemberIdAndOrderStatusByFuneralIdAndRoleCategoryId(
            memberId,
            funeral.id,
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

    fun getOrderByCompletionStatusAndDetailAndFuneralId(completionStatus: Boolean, detail: String, funeralId: Int): Order? {
        return vendorRepository.getOrderByCompletionStatusAndDetailAndFuneralId(completionStatus, detail, funeralId)
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

    fun getFemaleMourningClothBlacks(): List<MourningCloth> {
        return vendorRepository.getFemaleMourningClothBlacks()
    }

    fun getFemaleMourningClothWhites(): List<MourningCloth> {
        return vendorRepository.getFemaleMourningClothWhites()
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

    fun getFemaleMourningClothBlackById(femaleMourningClothBlackId: Int): MourningCloth? {
        return vendorRepository.getFemaleMourningClothBlackById(femaleMourningClothBlackId)
    }

    fun getFemaleMourningClothWhiteById(femaleMourningClothWhiteId: Int): MourningCloth? {
        return vendorRepository.getFemaleMourningClothWhiteById(femaleMourningClothWhiteId)
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

    fun callCoffinTransporter(clientId: Int, funeralId: Int, deceasedName: String, sex: String, frontNum: String, backNum: String, deceasedHomeAddress: String, departureAddress: String, destinationAddress: String, funeralHallName: String, department: String): ResultData {

        // 1) clientId로 funeral에 접근 후, funeral이 null인지 funeral의 담당 장례지도사가 맞는지 확인한다.
        val funeral = clientRepository.getFuneralByClientId(clientId)
        if(funeral == null || funeral.directorMemberId != rq.getLoginedMember()!!.id){
            return ResultData.from("F-1", "잘못된 접근입니다.")
        }

        // 2) 이미 운구차를 호출한 이력이 있는 경우, 다시 호출할 수 없다.
        if(funeral.coffinTransporterUseStatus){
            return ResultData.from("F-2", "운구차를 다시 호출할 수 없습니다.")
        }


        // 3) 필수 입력값 검사
        if(departureAddress.isBlank()){
            return ResultData.from("F-2", "운구차량 출동 주소를 입력해주세요.")
        }
        if(destinationAddress.isBlank() && funeralHallName.isBlank()){
            return ResultData.from("F-3", "장례식장을 선택하거나, 도착 주소를 입력해주세요.")
        }
        if(destinationAddress.isNotBlank() && funeralHallName.isNotBlank()){
            return ResultData.from("F-4", "장례식장 선택과 직접 입력 중, 하나만 입력해주십시오.")
        }




        // 해당 지역의 운구차업자에게 문자메세지 발송
        // 직업: 운구차 운전자 ( roleLevel = 4, roleCategoryId = 3 )
        val roleLevel = 4

        // 직업을 구분하기 위해 roleLevel 지역을 구분하기 위해 location을 매개변수로 받아, members를 출력
        // 추후 범용적으로 이 함수를 사용하기 위해 roleCategoryId를 넣었다. (0일 경우, roleCategoryId가 내부적으로 적용되지 않는다.)
        // 반대로, vendor의 경우, roleCategoryId 값을 넣으면 내부적으로 적용된다.
        val coffinTransporters: List<Member> = memberRepository.getMembersByLocationAndRole(department, roleLevel, 3)

        // location으로 조회했을 때, 해당 지역에 한 명도 없는 경우에 대한 예외처리
        if(coffinTransporters.isEmpty()){
            return ResultData.from("F-6", "${department}에 등록된 운구업자가 없습니다.")
        }

        // 발신자 전화번호
        val from = "01049219810"
        // 1명 이상의 수신자 전화번호 ( 알리고 API에서 수신인(receiver)로써 인식 가능한 상태로 넣어주는 함수 )
        // 다른 직업에 대해서도 재사용 가능
        val to = Ut.getCellphoneNosFromMembers(coffinTransporters)
        // 문자 내용
        val msg = "https://webroot/usr/vendor/coffinTransporterDispatch?clientId=${clientId} \n위 링크를 통해 위치를 확인하시고, 수락해주십시오."

        // 알리고 API에서 문자 전송에 필요한 데이터를 넘겨주고, 알리고로부터 반환된 결과값 rb
        val rb: Aligo__send__ResponseBody = Ut.sendSms(from, to.toString(), msg, true)

        // 4) 장례식장을 선택한 경우, 해당 장례식장을 통해 주소를 조회해서 destinationAddress 변수의 값으로 할당해준다.
        var destinationAddr = destinationAddress

        if(destinationAddress.isBlank() && funeralHallName.isNotBlank()){
            destinationAddr = vendorRepository.getFuneralHallAddrByName(funeralHallName)
        }
        // 선택적으로 입력된 client 정보를 수정한다.
        clientRepository.updateClientInCoffinTransporter(deceasedName, sex, frontNum, backNum, deceasedHomeAddress, clientId)

        // 운구차 테이블에 정보를 넣는다.
        vendorRepository.insertIntoCoffinTransporter(funeralId, departureAddress, destinationAddr)

        // 운구차 사용 여부를 true로 변경
        clientRepository.updateCoffinTransporterUseStatus(funeralId, true)

        // client 테이블에 운구차 departureAddress, destinationAddress 정보를 넣는다.
        val coffinTransporter = getCoffinTransporterByFuneralId(funeral.id)
        val funeralHall = memberRoleService.getFuneralHallByAddress(coffinTransporter!!.destinationAddress)
        clientService.modifyClientFuneralHallDeceasedAddress(clientId, funeralHall.name, departureAddress)


        return ResultData.from("S-1", "운구차 출동 요청이 완료되었습니다.")
    }

    fun getCoffinTransporterByFuneralId(id: Int): CoffinTransporter? {
        return vendorRepository.getCoffinTransporterByFuneralId(id)
    }

    fun doCoffinTransporterDispatch(clientId: Int): ResultData {
        // 현재 접속한 회원이 운구차 운전자가 아닌 경우는 잘못된 접근
        if(rq.getLoginedMember()!!.roleLevel != 4 || rq.getLoginedMember()!!.extra__roleCategoryId != 3){
            return ResultData.from("F-1", "잘못된 접근입니다.", "replaceUri", "/usr/home/main")
        }


        // 존재하지 않는 clientId를 URL로 접근하는 경우에 대한 예외처리
        val client = clientService.getClientById(clientId)
        val funeral = clientService.getFuneralByClientId(clientId)


        if (client == null || funeral == null) {
            return ResultData.from("F-1", "잘못된 접근입니다.", "replaceUri", "/usr/home/main")
        }

        val coffinTransporter = getCoffinTransporterByFuneralId(funeral.id)
        if(coffinTransporter == null){
            return ResultData.from("F-1", "잘못된 접근입니다.", "replaceUri", "/usr/home/main")
        }

        if(coffinTransporter.memberId == rq.getLoginedMember()!!.id){
            return ResultData.from("F-2", "이미 요청을 수락하셨습니다. 해당 페이지로 이동합니다.", "replaceUri", "/usr/vendor/coffinTransporterProgress?clientId=$clientId")
        }

        if(coffinTransporter.memberId != 0){
            return ResultData.from("F-3", "이미 다른 업자에 의해 수락되었습니다.", "replaceUri", "/usr/home/main")
        }

        // 현재 회원을 운구차 운전자로 배정
        vendorRepository.updateCoffinTransporter(rq.getLoginedMember()!!.id, funeral.id)

        // 장례지도사에게 연결된 사실을 알린다 (시작)

        val from = "01049219810"
        val directorMember = memberRepository.getMemberById(funeral.directorMemberId)
        val to = directorMember!!.cellphoneNo
        val msg = "https://webroot/usr/director/progress?clientId=${clientId} \n 운구업자가 연결되었습니다. 위 링크를 통해 연락처를 확인해주십시오."

        val rb: Aligo__send__ResponseBody = Ut.sendSms(from, to.toString(), msg, true)

        // 장례지도사에게 연결된 사실을 알린다 (끝)


        return ResultData.from("S-1", "출동 요청을 수락했습니다.", "replaceUri", "/usr/vendor/coffinTransporterProgress?clientId=$clientId")
    }

    fun doCoffinTransporterProgress(clientId: Int): ResultData {
        // 현재 접속한 회원이 운구차 운전자가 아닌 경우는 잘못된 접근
        if(rq.getLoginedMember()!!.roleLevel != 4 || rq.getLoginedMember()!!.extra__roleCategoryId != 3){
            return ResultData.from("F-1", "잘못된 접근입니다.", "replaceUri", "/usr/home/main")
        }


        // 존재하지 않는 clientId를 URL로 접근하는 경우에 대한 예외처리
        val client = clientService.getClientById(clientId)
        val funeral = clientService.getFuneralByClientId(clientId)


        if (client == null || funeral == null) {
            return ResultData.from("F-1", "잘못된 접근입니다.", "replaceUri", "/usr/home/main")
        }

        val coffinTransporter = getCoffinTransporterByFuneralId(funeral.id)
        if(coffinTransporter == null){
            return ResultData.from("F-1", "잘못된 접근입니다.", "replaceUri", "/usr/home/main")
        }
        if(coffinTransporter.memberId == 0){
            return ResultData.from("F-2", "접근 권한이 없습니다.", "replaceUri", "/usr/home/main")
        }

        if(coffinTransporter.memberId != rq.getLoginedMember()!!.id){
            return ResultData.from("F-2", "접근 권한이 없습니다.", "replaceUri", "/usr/home/main")
        }

        vendorRepository.updateCoffinTransporterComplete(funeral.id)


        // 운구 완료된 사실을 담당 장례지도사에게 통보(시작)

        val from = "01049219810"

        val to = memberRepository.getMemberById(funeral.directorMemberId)!!.cellphoneNo

        val msg = "https://webroot/usr/director/progress?clientId=${clientId} \n 고인의 운구가 완료되었습니다. 위 링크를 통해 장례 절차를 진행해주십시오."

        val rb: Aligo__send__ResponseBody = Ut.sendSms(from, to.toString(), msg, true)

        // 운구 완료된 사실을 담당 장례지도사에게 통보(끝)

        return ResultData.from("S-1", "완료 처리되었습니다.", "replaceUri", "/usr/home/main")
    }

    fun getCoffinNames(): List<String> {
        return vendorRepository.getCoffinNames()
    }

    fun getIncenses(): List<Mortuary> {
        return vendorRepository.getIncenses()
    }

    fun getCandles(): List<Mortuary> {
        return vendorRepository.getCandles()
    }

    fun getAncestralTablets(): List<Mortuary> {
        return vendorRepository.getAncestralTablets()
    }

    fun getCondolenceMoneyBooks(): List<Mortuary> {
        return vendorRepository.getCondolenceMoneyBooks()
    }

    fun getCondolenceBooks(): List<Mortuary> {
        return vendorRepository.getCondolenceBooks()
    }

    fun getPictureRibbons(): List<Mortuary> {
        return vendorRepository.getPictureRibbons()
    }

    fun getCoffinById(coffinId: Int): Coffin? {
        return vendorRepository.getCoffinById(coffinId)
    }

}
