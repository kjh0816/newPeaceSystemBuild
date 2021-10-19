package com.base.newPeaceSystemBuild.repository

import com.base.newPeaceSystemBuild.vo.client.Funeral
import com.base.newPeaceSystemBuild.vo.standard.*
import com.base.newPeaceSystemBuild.vo.vendor.Order
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update


@Mapper
interface VendorRepository {

    @Select(
        """
            SELECT * FROM coffinTransporter
        """
    )
    fun getCoffinTransporters(): List<CoffinTransporter>

    @Select(
    """
        SELECT * 
        FROM FLOWER
    """
    )
    fun getFlowers(): List<Flower>



    @Update(
        """
            UPDATE funeral 
            SET flowerId = #{flowerId} 
            WHERE id = #{funeralId};
        """
    )
    fun modifyFuneralIntoFlowerId(funeralId:Int, flowerId: Int)

    @Update(
        """
            UPDATE funeral 
            SET shroudId = #{shroudId} 
            WHERE id = #{funeralId};
        """
    )
    fun modifyFuneralIntoShroudId(funeralId: Int, shroudId: Int)

    @Select(
        """
            SELECT * 
            FROM funeral 
            WHERE id = #{funeralId};
        """
    )
    fun getFuneralById(funeralId: Int): Funeral?

    @Select(
        """
            SELECT *
            FROM `flower`
            WHERE id = #{flowerId}
        """
    )
    fun getFlowerById(flowerId: Int): Flower?

    @Select(
        """
            SELECT LAST_INSERT_ID();
        """
    )
    fun getLastInsertId(): Int

    @Insert(
        """
            INSERT INTO `order`
            SET regDate = NOW(),
            updateDate = NOW(),
            funeralId = #{funeralId},
            directorMemberId = #{directorMemberId},
            roleCategoryId = #{roleCategoryId},
            standardId = #{standardId},
            detail = #{detail}
        """
    )
    fun insertIntoOrder(funeralId: Int, directorMemberId: Int, roleCategoryId: Int, standardId: Int, detail: String)

    @Update(
        """
            UPDATE `order` SET
            vendorMemberId = #{vendorMemberId},
            orderStatus = #{orderStatus}
            WHERE directorMemberId = #{directorMemberId}
            AND roleCategoryId = #{roleCategoryId}
        """
    )
    fun modifyOrderIntoVendorMemberIdAndOrderStatusByDirectorMemberIdAndRoleCategoryId(vendorMemberId: Int, directorMemberId: Int, roleCategoryId: Int, orderStatus: Boolean)

    @Select(
        """
            SELECT * 
            FROM `order`
            WHERE funeralId = #{funeralId}
        """
    )
    fun getOrderByFuneralId(funeralId: Int): Order?

    @Select(
        """
            SELECT * 
            FROM `order`
            WHERE funeralId = #{funeralId}
            AND roleCategoryId = #{roleCategoryId}
            AND detail = #{detail}
        """
    )
    fun getOrderByFuneralIdAndRoleCategoryIdAndDetail(funeralId: Int, roleCategoryId: Int, detail: String): Order?

    @Select(
        """
            UPDATE `order`
            SET completionStatus = #{completionStatus}
            WHERE vendorMemberId = #{vendorMemberId}
            AND funeralId = #{funeralId}
        """
    )
    fun modifyOrderIntoCompleteStatusByVendorMemberIdAndFuneralId(vendorMemberId: Int, funeralId: Int, completionStatus: Boolean)

    @Select(
        """
            SELECT * 
            FROM flowerTribute
        """
    )
    fun getFlowerTributes(): List<FlowerTribute>

    @Insert(
        """
            INSERT INTO flowerTributeOrder
            SET regDate = NOW(),
            updateDate = NOW(),
            orderId = #{orderId},
            bunchCnt = #{bunchCnt},
            packing = #{packing}
        """
    )
    fun insertIntoFlowerTributeOrder(orderId: Int, bunchCnt: Int, packing: Boolean)

    @Update(
        """
            UPDATE funeral 
            SET flowerTributeId = #{flowerTributeId} 
            WHERE id = #{funeralId};
        """
    )
    fun modifyFuneralIntoFlowerTributeId(funeralId: Int, flowerTributeId: Int)

    @Select(
        """
            SELECT *
            FROM `flowerTribute`
            WHERE id = #{flowerTributeId} 
        """
    )
    fun getFlowerTributeById(flowerTributeId: Int): FlowerTribute?

    @Select(
        """
            <script>
            SELECT 
            O.*
            <if test="detail == 'flower'">
                , F.retailPrice AS `extra__retailPrice`
            </if>
            <if test="detail == 'flowerTribute'">
                , FT.retailPrice AS `extra__retailPrice`
                , FT.bunch AS `extra__bunch`
                , FTO.bunchCnt AS `extra__bunchCnt`
                , FTO.packing AS `extra__packing`
            </if>
            <if test="detail == 'femaleMourningClothBlack'">
                , FMCB.retailPrice AS `extra__retailPrice`
                , FMCBO.femaleClothCnt AS `extra__femaleClothBlackCnt`
            </if>
            <if test="detail == 'femaleMourningClothWhite'">
                , FMCW.retailPrice AS `extra__retailPrice`
                , FMCWO.femaleClothCnt AS `extra__femaleClothWhiteCnt`
            </if>
            <if test="detail == 'maleMourningCloth'">
                , MMC.retailPrice AS `extra__retailPrice`
                , MMCO.maleClothCnt AS `extra__maleClothCnt`
            </if>
            <if test="detail == 'shirt'">
                , S.retailPrice AS `extra__retailPrice`
                , SO.shirtCnt AS `extra__shirtCnt`
            </if>
            <if test="detail == 'necktie'">
                , N.retailPrice AS `extra__retailPrice`
                , NO.necktieCnt AS `extra__necktieCnt`
            </if>
            <if test="detail == 'coffinTransporter'">
                , CT.retailPrice AS `extra__retailPrice`
                , CTO.deceasedHomeAddress AS `extra__deceasedHomeAddress`
            </if>
            <if test="detail == 'shroud'">
                , S.retailPrice AS `extra__retailPrice`
            </if>
            FROM `order` AS O
            <if test="detail == 'flower'">
                LEFT JOIN flower AS F
                ON O.standardId = F.id
                LEFT JOIN flowerOrder AS FO
                ON O.id = FO.orderId
            </if>
            <if test="detail == 'flowerTribute'">
                LEFT JOIN flowerTribute AS FT
                ON O.standardId = FT.id
                LEFT JOIN flowerTributeOrder AS FTO
                ON O.id = FTO.orderId
            </if>
            <if test="detail == 'femaleMourningClothBlack'">
                LEFT JOIN femaleMourningClothBlack AS FMCB
                ON O.standardId = FMCB.id
                LEFT JOIN femaleMourningClothBlackOrder AS FMCBO
                ON O.id = FMCBO.orderId
            </if>
            <if test="detail == 'femaleMourningClothWhite'">
                LEFT JOIN femaleMourningClothWhite AS FMCW
                ON O.standardId = FMCW.id
                LEFT JOIN femaleMourningClothWhiteOrder AS FMCWO
                ON O.id = FMCWO.orderId
            </if>
            <if test="detail == 'maleMourningCloth'">
                LEFT JOIN maleMourningCloth AS MMC
                ON O.standardId = MMC.id
                LEFT JOIN maleMourningClothOrder AS MMCO
                ON O.id = MMCO.orderId
            </if>
            <if test="detail == 'shirt'">
                LEFT JOIN shirt AS S
                ON O.standardId = S.id
                LEFT JOIN shirtOrder AS SO
                ON O.id = SO.orderId
            </if>
            <if test="detail == 'necktie'">
                LEFT JOIN necktie AS N
                ON O.standardId = N.id
                LEFT JOIN necktieOrder AS NO
                ON O.id = NO.orderId
            </if>
            <if test="detail == 'coffinTransporter'">
                LEFT JOIN coffinTransporter AS CT
                ON O.standardId = CT.id
                LEFT JOIN coffinTransporterOrder AS CTO
                ON O.id = CTO.orderId
            </if>
            <if test="detail == 'shroud'">
                LEFT JOIN shroud AS S
                ON O.standardId = S.id
                LEFT JOIN shroudOrder AS SO
                ON O.id = SO.orderId
            </if>
            WHERE O.vendorMemberId = #{vendorMemberId}
            AND O.orderStatus = #{orderStatus}
            AND O.completionStatus = #{completionStatus}
            AND O.roleCategoryId = #{roleCategoryId}
            AND O.detail = #{detail}
            </script>
        """
    )
    fun getOrdersByVendorMemberIdAndOrderStatus(vendorMemberId: Int, roleCategoryId: Int, orderStatus: Boolean, completionStatus: Boolean, detail: String): List<Order>

    @Select(
        """
            <script>
            SELECT 
            O.*
            <if test="detail == 'flower'">
                , F.retailPrice AS `extra__retailPrice`
            </if>
            <if test="detail == 'flowerTribute'">
                , FT.retailPrice AS `extra__retailPrice`
                , FT.bunch AS `extra__bunch`
                , FTO.bunchCnt AS `extra__bunchCnt`
                , FTO.packing AS `extra__packing`
            </if>
            <if test="detail == 'femaleMourningClothBlack'">
                , FMCB.retailPrice AS `extra__retailPrice`
                , FMCBO.femaleClothCnt AS `extra__femaleClothBlackCnt`
            </if>
            <if test="detail == 'femaleMourningClothWhite'">
                , FMCW.retailPrice AS `extra__retailPrice`
                , FMCWO.femaleClothCnt AS `extra__femaleClothWhiteCnt`
            </if>
            <if test="detail == 'maleMourningCloth'">
                , MMC.retailPrice AS `extra__retailPrice`
                , MMCO.maleClothCnt AS `extra__maleClothCnt`
            </if>
            <if test="detail == 'shirt'">
                , S.retailPrice AS `extra__retailPrice`
                , SO.shirtCnt AS `extra__shirtCnt`
            </if>
            <if test="detail == 'necktie'">
                , N.retailPrice AS `extra__retailPrice`
                , NO.necktieCnt AS `extra__necktieCnt`
            </if>
            <if test="detail == 'coffinTransporter'">
                , CT.retailPrice AS `extra__retailPrice`
                , CTO.deceasedHomeAddress AS `extra__deceasedHomeAddress`
            </if>
            <if test="detail == 'shroud'">
                , S.retailPrice AS `extra__retailPrice`
            </if>
            FROM `order` AS O
            <if test="detail == 'flower'">
                LEFT JOIN flower AS F
                ON O.standardId = F.id
                LEFT JOIN flowerOrder AS FO
                ON O.id = FO.orderId
            </if>
            <if test="detail == 'flowerTribute'">
                LEFT JOIN flowerTribute AS FT
                ON O.standardId = FT.id
                LEFT JOIN flowerTributeOrder AS FTO
                ON O.id = FTO.orderId
            </if>
            <if test="detail == 'femaleMourningClothBlack'">
                LEFT JOIN femaleMourningClothBlack AS FMCB
                ON O.standardId = FMCB.id
                LEFT JOIN femaleMourningClothBlackOrder AS FMCBO
                ON O.id = FMCBO.orderId
            </if>
            <if test="detail == 'femaleMourningClothWhite'">
                LEFT JOIN femaleMourningClothWhite AS FMCW
                ON O.standardId = FMCW.id
                LEFT JOIN femaleMourningClothWhiteOrder AS FMCWO
                ON O.id = FMCWO.orderId
            </if>
            <if test="detail == 'maleMourningCloth'">
                LEFT JOIN maleMourningCloth AS MMC
                ON O.standardId = MMC.id
                LEFT JOIN maleMourningClothOrder AS MMCO
                ON O.id = MMCO.orderId
            </if>
            <if test="detail == 'shirt'">
                LEFT JOIN shirt AS S
                ON O.standardId = S.id
                LEFT JOIN shirtOrder AS SO
                ON O.id = SO.orderId
            </if>
            <if test="detail == 'necktie'">
                LEFT JOIN necktie AS N
                ON O.standardId = N.id
                LEFT JOIN necktieOrder AS NO
                ON O.id = NO.orderId
            </if>
            <if test="detail == 'coffinTransporter'">
                LEFT JOIN coffinTransporter AS CT
                ON O.standardId = CT.id
                LEFT JOIN coffinTransporterOrder AS CTO
                ON O.id = CTO.orderId
            </if>
            <if test="detail == 'shroud'">
                LEFT JOIN shroud AS S
                ON O.standardId = S.id
                LEFT JOIN shroudOrder AS SO
                ON O.id = SO.orderId
            </if>
            WHERE O.directorMemberId = #{directorMemberId}
            AND completionStatus = #{completionStatus}
            AND detail = #{detail}
            AND funeralId = #{funeralId}
            </script>
        """
    )
    fun getOrderByDirectorMemberIdAndCompletionStatusAndDetailAndFuneralId(
        directorMemberId: Int,
        completionStatus: Boolean,
        detail: String,
        funeralId: Int
    ): Order?

    @Select(
        """
            <script>
            SELECT 
            O.*
            <if test="detail == 'flower'">
                , F.retailPrice AS `extra__retailPrice`
            </if>
            <if test="detail == 'flowerTribute'">
                , FT.retailPrice AS `extra__retailPrice`
                , FT.bunch AS `extra__bunch`
                , FTO.bunchCnt AS `extra__bunchCnt`
                , FTO.packing AS `extra__packing`
            </if>
            <if test="detail == 'femaleMourningClothBlack'">
                , FMCB.retailPrice AS `extra__retailPrice`
                , FMCBO.femaleClothCnt AS `extra__femaleClothBlackCnt`
            </if>
            <if test="detail == 'femaleMourningClothWhite'">
                , FMCW.retailPrice AS `extra__retailPrice`
                , FMCWO.femaleClothCnt AS `extra__femaleClothWhiteCnt`
            </if>
            <if test="detail == 'maleMourningCloth'">
                , MMC.retailPrice AS `extra__retailPrice`
                , MMCO.maleClothCnt AS `extra__maleClothCnt`
            </if>
            <if test="detail == 'shirt'">
                , S.retailPrice AS `extra__retailPrice`
                , SO.shirtCnt AS `extra__shirtCnt`
            </if>
            <if test="detail == 'necktie'">
                , N.retailPrice AS `extra__retailPrice`
                , NO.necktieCnt AS `extra__necktieCnt`
            </if>
            <if test="detail == 'coffinTransporter'">
                , CT.retailPrice AS `extra__retailPrice`
                , CTO.deceasedHomeAddress AS `extra__deceasedHomeAddress`
            </if>
            <if test="detail == 'shroud'">
                , S.retailPrice AS `extra__retailPrice`
            </if>
            FROM `order` AS O
            <if test="detail == 'flower'">
                LEFT JOIN flower AS F
                ON O.standardId = F.id
                LEFT JOIN flowerOrder AS FO
                ON O.id = FO.orderId
            </if>
            <if test="detail == 'flowerTribute'">
                LEFT JOIN flowerTribute AS FT
                ON O.standardId = FT.id
                LEFT JOIN flowerTributeOrder AS FTO
                ON O.id = FTO.orderId
            </if>
            <if test="detail == 'femaleMourningClothBlack'">
                LEFT JOIN femaleMourningClothBlack AS FMCB
                ON O.standardId = FMCB.id
                LEFT JOIN femaleMourningClothBlackOrder AS FMCBO
                ON O.id = FMCBO.orderId
            </if>
            <if test="detail == 'femaleMourningClothWhite'">
                LEFT JOIN femaleMourningClothWhite AS FMCW
                ON O.standardId = FMCW.id
                LEFT JOIN femaleMourningClothWhiteOrder AS FMCWO
                ON O.id = FMCWO.orderId
            </if>
            <if test="detail == 'maleMourningCloth'">
                LEFT JOIN maleMourningCloth AS MMC
                ON O.standardId = MMC.id
                LEFT JOIN maleMourningClothOrder AS MMCO
                ON O.id = MMCO.orderId
            </if>
            <if test="detail == 'shirt'">
                LEFT JOIN shirt AS S
                ON O.standardId = S.id
                LEFT JOIN shirtOrder AS SO
                ON O.id = SO.orderId
            </if>
            <if test="detail == 'necktie'">
                LEFT JOIN necktie AS N
                ON O.standardId = N.id
                LEFT JOIN necktieOrder AS NO
                ON O.id = NO.orderId
            </if>
            <if test="detail == 'coffinTransporter'">
                LEFT JOIN coffinTransporter AS CT
                ON O.standardId = CT.id
                LEFT JOIN coffinTransporterOrder AS CTO
                ON O.id = CTO.orderId
            </if>
            <if test="detail == 'shroud'">
                LEFT JOIN shroud AS S
                ON O.standardId = S.id
                LEFT JOIN shroudOrder AS SO
                ON O.id = SO.orderId
            </if>
            WHERE O.funeralId = #{funeralId}
            AND O.completionStatus = #{completionStatus}
            AND O.detail = #{detail};
            </script>
        """
    )
    fun getOrderByFuneralIdAndCompletionStatusAndDetail(
        funeralId: Int,
        completionStatus: Boolean,
        detail: String
    ): Order?


    @Insert(
        """
            INSERT INTO flowerOrder
            SET regDate = NOW(),
            updateDate = NOW(),
            orderId = #{orderId}
        """
    )
    fun insertIntoFlowerOrder(orderId: Int)

    @Insert(
        """
            INSERT INTO shroudOrder
            SET regDate = NOW(),
            updateDate = NOW(),
            orderId = #{orderId}
        """
    )
    fun insertIntoShroudOrder(orderId: Int)



    @Update(
        """
            UPDATE `order`
            SET updateDate = NOW(),
            standardId = #{standardId}
            WHERE funeralId = #{funeralId}
            AND directorMemberId = #{directorMemberId}
            AND roleCategoryId = #{roleCategoryId}
            AND detail = #{detail}
            AND completionStatus = #{completionStatus}
        """
    )
    fun modifyOrderIntoStandardIdByFuneralIdDirectorMemberIdRoleCategoryIdDetailCompletionStatus(
        standardId: Int,
        funeralId: Int,
        directorMemberId: Int,
        roleCategoryId: Int,
        detail: String,
        completionStatus: Boolean
    )

    @Update(
        """
            UPDATE flowerTributeOrder
            SET updateDate = NOW(),
            bunchCnt = #{bunchCnt},
            packing = #{packing}
            WHERE orderId = #{orderId}
        """
    )
    fun modifyFlowerTributeOrderIntoBunchCntAndPackingByOrderId(bunchCnt: Int, packing: Boolean, orderId: Int)


    @Select(
        """
            SELECT * FROM `femaleMourningClothBlack`
        """
    )
    fun getFemaleMourningClothBlacks(): List<MourningCloth>

    @Select(
        """
            SELECT * FROM `femaleMourningClothWhite`
        """
    )
    fun getFemaleMourningClothWhites(): List<MourningCloth>

    @Select(
        """
            SELECT * FROM `maleMourningCloth`
        """
    )
    fun getMaleMourningCloths(): List<MourningCloth>

    @Select(
        """
            SELECT * FROM `shirt`
        """
    )
    fun getShirts(): List<MourningCloth>

    @Select(
        """
            SELECT * FROM `necktie`
        """
    )
    fun getNeckties(): List<MourningCloth>

    @Insert(
        """
            INSERT INTO femaleMourningClothBlackOrder
            SET regDate = NOW(),
            updateDate = NOW(),
            orderId = #{orderId},
            femaleClothCnt = #{femaleClothCnt}
        """
    )
    fun insertIntoFemaleMourningClothBlackOrder(
        orderId: Int,
        femaleClothCnt: Int
    )

    @Insert(
        """
            INSERT INTO femaleMourningClothWhiteOrder
            SET regDate = NOW(),
            updateDate = NOW(),
            orderId = #{orderId},
            femaleClothCnt = #{femaleClothCnt}
        """
    )
    fun insertIntoFemaleMourningClothWhiteOrder(
        orderId: Int,
        femaleClothCnt: Int
    )

    @Update(
        """
            UPDATE femaleMourningClothBlackOrder
            SET updateDate = NOW(),
            femaleClothCnt = #{femaleClothCnt}
            WHERE orderId = #{orderId}
        """
    )
    fun modifyFemaleMourningClothBlackOrderIntoFemaleClothCntAndFemaleClothColorByOrderId(
        femaleClothCnt: Int,
        orderId: Int
    )

    @Update(
        """
            UPDATE femaleMourningClothWhiteOrder
            SET updateDate = NOW(),
            femaleClothCnt = #{femaleClothCnt}
            WHERE orderId = #{orderId}
        """
    )
    fun modifyFemaleMourningClothWhiteOrderIntoFemaleClothCntAndFemaleClothColorByOrderId(
        femaleClothCnt: Int,
        orderId: Int
    )

    @Insert(
        """
            INSERT INTO maleMourningClothOrder
            SET regDate = NOW(),
            updateDate = NOW(),
            orderId = #{orderId},
            maleClothCnt = #{maleClothCnt}
        """
    )
    fun insertIntoMaleMourningClothOrder(orderId: Int, maleClothCnt: Int)

    @Update(
        """
            UPDATE maleMourningClothOrder
            SET updateDate = NOW(),
            maleClothCnt = #{maleClothCnt}
            WHERE orderId = #{orderId}
        """
    )
    fun modifyMaleMourningClothOrderIntoMaleClothCntByOrderId(maleClothCnt: Int, orderId: Int)

    @Update(
        """
            UPDATE funeral 
            SET femaleMourningClothBlackId = #{femaleMourningClothBlackId} 
            WHERE id = #{funeralId};
        """
    )
    fun modifyFuneralIntoFemaleMourningClothBlackId(funeralId: Int, femaleMourningClothBlackId: Int)

    @Update(
        """
            UPDATE funeral 
            SET femaleMourningClothWhiteId = #{femaleMourningClothWhiteId} 
            WHERE id = #{funeralId};
        """
    )
    fun modifyFuneralIntoFemaleMourningClothWhiteId(funeralId: Int, femaleMourningClothWhiteId: Int)

    @Update(
        """
            UPDATE funeral 
            SET maleMourningClothId = #{maleMourningClothId} 
            WHERE id = #{funeralId};
        """
    )
    fun modifyFuneralIntoMaleMourningClothId(funeralId: Int, maleMourningClothId: Int)

    @Select(
        """
            SELECT *
            FROM `femaleMourningClothBlack`
            WHERE id = #{femaleMourningClothBlackId}
        """
    )
    fun getFemaleMourningClothBlackById(femaleMourningClothBlackId: Int): MourningCloth?

    @Select(
        """
            SELECT *
            FROM `femaleMourningClothWhite`
            WHERE id = #{femaleMourningClothWhiteId}
        """
    )
    fun getFemaleMourningClothWhiteById(femaleMourningClothWhiteId: Int): MourningCloth?

    @Select(
        """
            SELECT *
            FROM `maleMourningCloth`
            WHERE id = #{maleMourningClothId}
        """
    )
    fun getMaleMourningClothById(maleMourningClothId: Int): MourningCloth?

    @Select(
        """
            SELECT *
            FROM `shirt`
            WHERE id = #{shirtId}
        """
    )
    fun getShirtById(shirtId: Int): MourningCloth?

    @Select(
        """
            SELECT *
            FROM `necktie`
            WHERE id = #{necktieId}
        """
    )
    fun getNecktieById(necktieId: Int): MourningCloth?

    @Select(
        """
            SELECT *
            FROM `shroud`
            WHERE id = #{shroudId}
        """
    )
    fun getShroudById(shroudId: Int): Shroud?

    @Insert(
        """
            INSERT INTO shirtOrder
            SET regDate = NOW(),
            updateDate = NOW(),
            orderId = #{orderId},
            shirtCnt = #{shirtCnt}
        """
    )
    fun insertIntoShirtOrder(orderId: Int, shirtCnt: Int)

    @Update(
        """
            UPDATE shirtOrder
            SET updateDate = NOW(),
            shirtCnt = #{shirtCnt}
            WHERE orderId = #{orderId}
        """
    )
    fun modifyShirtOrderIntoShirtCntByOrderId(shirtCnt: Int, orderId: Int)

    @Update(
        """
            UPDATE funeral 
            SET shirtId = #{shirtId} 
            WHERE id = #{funeralId};
        """
    )
    fun modifyFuneralIntoShirtId(funeralId: Int, shirtId: Int)

    @Insert(
        """
            INSERT INTO necktieOrder
            SET regDate = NOW(),
            updateDate = NOW(),
            orderId = #{orderId},
            necktieCnt = #{necktieCnt}
        """
    )
    fun insertIntoNecktieOrder(orderId: Int, necktieCnt: Int)

    @Update(
        """
            UPDATE necktieOrder
            SET updateDate = NOW(),
            necktieCnt = #{necktieCnt}
            WHERE orderId = #{orderId}
        """
    )
    fun modifyNecktieOrderIntoNecktieCntByOrderId(necktieCnt: Int, orderId: Int)

    @Update(
        """
            UPDATE funeral 
            SET necktieId = #{necktieId} 
            WHERE id = #{funeralId};
        """
    )
    fun modifyFuneralIntoNecktieId(funeralId: Int, necktieId: Int)

    @Select(
        """
            SELECT * FROM shroud
        """
    )
    fun getShrouds(): List<Shroud>
    @Select(
            """
                SELECT address
                FROM funeralHall
                WHERE name = #{funeralHallName}
            """
    )
    fun getFuneralHallAddrByName(funeralHallName: String): String
    @Insert(
            """
                INSERT INTO coffinTransporter
                SET regDate = NOW(),
                updateDate = NOW(),
                funeralId = #{funeralId},
                departureAddress = #{departureAddress},
                destinationAddress = #{destinationAddr}
            """
    )
    fun insertIntoCoffinTransporter(funeralId: Int, departureAddress: String, destinationAddr: String)

    @Select(
            """
                SELECT *
                FROM coffinTransporter
                WHERE funeralId = #{id}
            """
    )
    fun getCoffinTransporterByFuneralId(id: Int): CoffinTransporter?

    @Update(
            """
                UPDATE coffinTransporter
                SET updateDate = NOW(),
                memberId = #{memberId}
                WHERE funeralId = #{funeralId}
            """
    )
    fun updateCoffinTransporter(memberId: Int, funeralId: Int)
    @Update(
            """
                UPDATE coffinTransporter
                SET updateDate = NOW(),
                completionStatus = true
                WHERE funeralId = #{funeralId)
            """
    )
    fun updateCoffinTransporterComplete(funeralId: Int)


}