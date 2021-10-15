package com.base.newPeaceSystemBuild.repository

import com.base.newPeaceSystemBuild.vo.client.Client
import com.base.newPeaceSystemBuild.vo.client.Family
import com.base.newPeaceSystemBuild.vo.client.Funeral
import com.base.newPeaceSystemBuild.vo.standard.CoffinTransporter
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

    // 상주 정보를 clientId롤 통해 조회(상주는 항상 1명만 있도록 로직이 짜여있음)
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
    
    // 상주 정보를 유가족 정보에 입력했는지 확인하기 위해 chiefStatus에 상관없이 조회
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
    @Select(
            """
                SELECT *
                FROM family
                WHERE name = #{familyName}
                AND relation = #{familyRelation}
                AND cellphoneNo = #{familyCellphoneNo}
                AND clientId = #{clientId}
                AND chiefStatus = 0
            """
    )
    fun getFamilyMemberByAll(familyRelation: String, familyName: String, familyCellphoneNo: String, clientId: Int): Family


    @Update(
            """
                UPDATE client
                SET updateDate = NOW(),
                deceasedName = #{deceasedName},
                sex = #{sex},
                deceasedHomeAddress = #{deceasedHomeAddress},
                deceasedDate = #{deceasedDate},
                frontNum = #{frontNum},
                backNum = #{backNum},
                birth = #{birth},
                lunar = #{lunar},
                funeralHall = #{funeralHallName},
                funeralHallRoom = #{funeralHallRoom},
                familyClan = #{familyClan},
                religion = #{religion},
                duty = #{duty},
                funeralMethod = #{funeralMethod},
                cremationLocation = #{cremationLocation},
                buryLocation = #{buryLocation},
                cause = #{cause},
                papers = #{papers},
                autopsyCheck = #{autopsyCheck},
                casketDate = #{casketDate},
                casketTime = #{casketTime},
                leavingDate = #{leavingDate},
                leavingTime = #{leavingTime}
                WHERE id = #{clientId} 
            """
    )
    fun modifyClient(funeralHallName: String, funeralHallRoom: String, deceasedName: String, frontNum: String, backNum: String, deceasedHomeAddress: String, familyClan: String, religion: String, duty: String, birth: String, deceasedDate: String, lunar: Int, funeralMethod: Int, cremationLocation: String, buryLocation: String, cause: String, papers: Int, autopsyCheck: Boolean, casketDate: String, casketTime: String, leavingDate: String, leavingTime: String, chiefName: String, chiefRelation: String, chiefCellphoneNo: String, chiefAddress: String, clientId: Int, sex: Char)

    @Select(
            """
                SELECT *
                FROM coffinTransporter
                WHERE funeralId = #{id}
            """
    )
    fun getCoffinTransporterByFuneralId(id: Int): CoffinTransporter

    @Update(
            """
                UPDATE client
                SET updateDate = NOW(),
                sex = #{sex},
                deceasedName = #{deceasedName},
                frontNum = #{frontNum},
                backNum = #{backNum},
                deceasedHomeAddress = #{deceasedHomeAddress}
            """
    )
    fun updateClientInCoffinTransporter(deceasedName: String, sex: String, frontNum: String, backNum: String, deceasedHomeAddress: String)
    @Update(
        """
            UPDATE funeral
            SET updateDate = NOW(),
            coffinTransporterUseStatus = #{bool}
            WHERE id = #{funeralId}
        """
    )
    fun updateCoffinTransporterUseStatus(funeralId: Int, bool: Boolean)


}
