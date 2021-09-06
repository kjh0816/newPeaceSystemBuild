package com.base.newPeaceSystemBuild.repository

import com.base.newPeaceSystemBuild.vo.client.Funeral
import com.base.newPeaceSystemBuild.vo.standard.Flower
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
    fun getFuneralById(funeralId: Int): Funeral

    @Select(
        """
            SELECT *
            FROM `flower`
            WHERE id = #{flowerId}
        """
    )
    fun getFlowerById(flowerId: Int): Flower

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
            standardId = #{flowerId}
        """
    )
    fun insertIntoOrder(clientId:Int, directorMemberId: Int, roleCategoryId: Int, flowerId: Int)

    @Update(
        """
            UPDATE `order` SET
            vendorMemberId = #{vendorMemberId}
            WHERE directorMemberId = #{directorMemberId}
        """
    )
    fun modifyOrderIntoVendorMemberIdByDirectorMemberId(vendorMemberId: Int, directorMemberId: Int)

    @Select(
        """
            SELECT 
            O.*,
            C.id AS `extra__clientId`,
            C.regDate AS `extra__clientRegDate`,
            C.updateDate AS `extra__clientUpdateDate`,
            C.deceasedName AS `extra__deceasedName`,
            C.relatedName AS `extra__relatedName`,
            C.cellphoneNo AS `extra__cellphoneNo`,
            C.location AS `extra__location`,
            C.address AS `extra__address`,
            C.bank AS `extra__bank`,
            C.accountNum AS `extra__accountNum`,
            M.name AS `extra__directorName`,
            M.cellphoneNo AS `extra__directorCellphoneNo`,
            F.name AS `extra__flowerName`,
            F.retailPrice AS `extra__flowerRetailPrice`
            FROM `order` AS O
            LEFT JOIN `client` AS C
            ON O.clientId = C.id
            LEFT JOIN `member` AS M
            ON O.directorMemberId = M.id
            LEFT JOIN `flower` AS F
            ON O.standardId = F.id
            WHERE vendorMemberId = #{vendorMemberId}
            AND orderStatus = #{orderStatus}
        """
    )
    fun getOrdersByVendorMemberIdAndOrderStatus(vendorMemberId: Int, orderStatus: Boolean): List<Order>
}