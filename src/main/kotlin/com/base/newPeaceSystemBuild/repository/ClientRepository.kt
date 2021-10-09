package com.base.newPeaceSystemBuild.repository

import com.base.newPeaceSystemBuild.vo.client.Client
import com.base.newPeaceSystemBuild.vo.client.Family
import com.base.newPeaceSystemBuild.vo.client.Funeral
import org.apache.ibatis.annotations.*

@Mapper
interface ClientRepository {
    @Select(
        """
            SELECT LAST_INSERT_ID();
        """
    )
    fun getLastInsertId(): Int

    @Insert(
        """
            INSERT INTO `client`
            SET regDate = NOW(),
            updateDate = NOW(),
            memberId = #{memberId},
            deceasedName = #{deceasedName},
            location = #{location},
            briefAddress = #{briefAddress}
        """
    )
    fun insertIntoClient(memberId: Int, deceasedName: String, location: String, briefAddress: String)

    @Select(
        """
            SELECT *
            FROM `client`
            WHERE id = #{clientId}
        """
    )
    fun getClientById(clientId: Int): Client?
    @Select(
        """
            SELECT *
            FROM funeral
            WHERE clientId = #{clientId}
        """
    )
    fun getFuneralByClientId(clientId: Int): Funeral?

    @Select(
        """
            SELECT * 
            FROM `funeral` 
            WHERE progress = 1 
            AND directorMemberId = #{directorMemberId}
            
        """
    )
    fun getProgressingFuneralByDirectorMemberId(directorMemberId: Int): Funeral?

    @Update(
        """
            UPDATE `client`
            SET directorMemberId = #{directorMemberId}
            WHERE id = #{clientId};
        """
    )
    fun modifyClientIntoDirectorMemberIdByClientId(directorMemberId: Int, clientId: Int)

    @Insert(
        """
            INSERT INTO funeral
            SET regDate = NOW(),
            updateDate =NOW(),
            clientId = #{clientId},
            directorMemberId = #{directorMemberId},
            memberId = #{memberId},
            progress = 1
        """
    )
    fun insertFuneral(memberId: Int, directorMemberId: Int, clientId: Int)

    @Select(
        """
            SELECT *
            FROM funeral
            WHERE directorMemberId = #{directorMemberId}
            AND progress = 1
        """
    )
    fun getProgressingFuneralByIdDirectorMemberId(directorMemberId: Int): Funeral?

    @Select(
        """
            SELECT CONCAT(
            SUBSTR(cellphoneNo, 1, 3)
            , '-'
            , SUBSTR(cellphoneNo, 4, 4)
            , '-'
            , SUBSTR(cellphoneNo, 8, 4)
            ) AS `cellphoneNo`
            FROM `client`
            WHERE id = #{id};
        """
    )
    fun getCellphoneNoFormatted(id: Int): String
    @Insert(
            """
                INSERT INTO family
                SET regDate = NOW(),
                updateDate = NOW(),
                clientId = #{clientId},
                chiefStatus = 1,
                `name` = #{relatedName},
                cellphoneNo = #{cellphoneNo}
            """
    )
    fun insertIntoFamily(clientId: Int, relatedName: String, cellphoneNo: String)

    @Select(
            """
                SELECT *
                FROM family
                WHERE clientId = #{clientId}
                AND chiefStatus = 1
            """
    )
    fun getFamilyByClientId(clientId: Int): Family

    @Select(
        """
            SELECT *
            FROM funeral
            WHERE directorMemberId = #{directorMemberId}
            AND progress = #{progress}
        """
    )
    fun getFuneralsByDirectorMemberIdAndProgress(directorMemberId: Int, progress: Boolean): List<Funeral>

    @Select(
            """
                SELECT *
                FROM family
                WHERE name = #{familyName}
                AND relation = #{familyRelation}
                AND cellphoneNo = #{familyCellphoneNo}
                AND clientId = #{clientId}
            """
    )
    fun getFamilyByClientIdAndAll(familyRelation: String, familyName: String, familyCellphoneNo: String, clientId: Int): Family

    @Insert(
            """
                INSERT INTO family
                SET regDate = NOW(),
                updateDate = NOW(),
                clientId = #{clientId},
                name = #{familyName},
                relation = #{familyRelation},
                cellphoneNo = #{familyCellphoneNo}
            """
    )
    fun addFamily(familyRelation: String, familyName: String, familyCellphoneNo: String, clientId: Int)

    @Select(
            """
                SELECT *
                FROM family
                WHERE clientId = #{clientId}
                AND chiefStatus = 0
            """
    )
    fun getFamilyMembersByClientId(clientId: Int): List<Family>

    @Delete(
            """
                DELETE FROM family
                WHERE name = #{familyName}
                AND relation = #{familyRelation}
                AND cellphoneNo = #{familyCellphoneNo}
                AND clientId = #{clientId}
                AND chiefStatus = 0
            """
    )
    fun removeFamily(familyRelation: String, familyName: String, familyCellphoneNo: String, clientId: Int)


}
