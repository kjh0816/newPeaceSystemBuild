package com.base.newPeaceSystemBuild.repository

import com.base.newPeaceSystemBuild.vo.client.Funeral
import com.base.newPeaceSystemBuild.vo.standard.Flower
import com.base.newPeaceSystemBuild.vo.standard.FlowerTribute
import com.base.newPeaceSystemBuild.vo.vendor.Order
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update


@Mapper
interface VendorRepository {

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
            clientId = #{clientId},
            directorMemberId = #{directorMemberId},
            roleCategoryId = #{roleCategoryId},
            standardId = #{standardId},
            detail = #{detail}
        """
    )
    fun insertIntoOrder(clientId:Int, directorMemberId: Int, roleCategoryId: Int, standardId: Int, detail: String)

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
            FROM `order` AS O
            <if test="detail == 'flower'">
                LEFT JOIN flowerOrder AS FO
                ON O.id = FO.orderId
                LEFT JOIN flower AS F
                ON O.standardId = F.id
            </if>
            <if test="detail == 'flowerTribute'">
                LEFT JOIN flowerTributeOrder AS FTO
                ON O.id = FTO.orderId
                LEFT JOIN flowerTribute AS FT
                ON O.standardId = FT.id
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
            SELECT * 
            FROM `order`
            WHERE clientId = #{clientId}
        """
    )
    fun getOrderByClientId(clientId: Int): Order?

    @Select(
        """
            SELECT * 
            FROM `order`
            WHERE clientId = #{clientId}
            AND roleCategoryId = #{roleCategoryId}
            AND detail = #{detail}
        """
    )
    fun getOrderByClientIdAndRoleCategoryIdAndDetail(clientId: Int, roleCategoryId: Int, detail: String): Order?

    @Select(
        """
            UPDATE `order`
            SET completionStatus = #{completionStatus}
            WHERE vendorMemberId = #{vendorMemberId}
            AND clientId = #{clientId}
        """
    )
    fun modifyOrderIntoCompleteStatusByVendorMemberIdAndClientId(vendorMemberId: Int, clientId: Int, completionStatus: Boolean)

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
            SELECT 
            O.*,
            FTO.bunchCnt AS `extra__bunchCnt`,
            FTO.packing AS `extra__packing`
            FROM `order` AS O
            LEFT JOIN flowerTributeOrder AS FTO
            ON O.id = FTO.orderId
            WHERE O.directorMemberId = #{directorMemberId}
            AND FTO.bunchCnt IS NOT NULL
            AND FTO.packing IS NOT NULL
        """
    )
    fun getFlowerTributeOrderByDirectorMemberId(directorMemberId: Int): Order

    @Insert(
        """
            INSERT INTO flowerOrder
            SET regDate = NOW(),
            updateDate = NOW(),
            orderId = #{orderId}
        """
    )
    fun insertIntoFlowerOrder(orderId: Int)


}