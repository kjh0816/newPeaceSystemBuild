package com.base.newPeaceSystemBuild.repository

import com.base.newPeaceSystemBuild.vo.client.Client
import com.base.newPeaceSystemBuild.vo.client.Funeral
import com.base.newPeaceSystemBuild.vo.member.Bank
import com.base.newPeaceSystemBuild.vo.member.Department
import com.base.newPeaceSystemBuild.vo.member.Member
import com.base.newPeaceSystemBuild.vo.member.Role
import org.apache.ibatis.annotations.*

@Mapper
interface MemberRepository {
    @Select(
        """
            SELECT 
            M.*,
            MR.regDate AS `extra__regDate`,
            MR.updateDate AS `extra__updateDate`,
            MR.introduce AS `extra__introduce`,
            MR.authenticationLevel AS `extra__authenticationLevel`,
            MR.authenticationDate AS `extra__authenticationDate`,
            MR.roleCategoryId AS `extra__roleCategoryId`,
            R.roleName AS `extra__roleName`
            FROM `member` AS M
            LEFT JOIN memberRole AS MR
            ON M.id = MR.memberId
            LEFT JOIN `role` AS R
            ON M.roleLevel = R.id
            WHERE M.loginId = #{loginId}
        """
    )
    fun getMemberByLoginId(@Param("loginId") loginId: String): Member?

    @Select(
        """
            SELECT 
            M.*,
            MR.regDate AS `extra__regDate`,
            MR.updateDate AS `extra__updateDate`,
            MR.introduce AS `extra__introduce`,
            MR.authenticationLevel AS `extra__authenticationLevel`,
            MR.authenticationDate AS `extra__authenticationDate`,
            MR.roleCategoryId AS `extra__roleCategoryId`,
            R.roleName AS `extra__roleName`
            FROM `member` AS M
            LEFT JOIN memberRole AS MR
            ON M.id = MR.memberId
            LEFT JOIN `role` AS R
            ON M.roleLevel = R.id
            WHERE M.email = #{email}
        """
    )
    fun getMemberByEmail(email: String): Member?

    @Select(
        """
            SELECT 
            M.*,
            MR.regDate AS `extra__regDate`,
            MR.updateDate AS `extra__updateDate`,
            MR.introduce AS `extra__introduce`,
            MR.authenticationLevel AS `extra__authenticationLevel`,
            MR.authenticationDate AS `extra__authenticationDate`,
            MR.roleCategoryId AS `extra__roleCategoryId`,
            R.roleName AS `extra__roleName`
            FROM `member` AS M
            LEFT JOIN memberRole AS MR
            ON M.id = MR.memberId
            LEFT JOIN `role` AS R
            ON M.roleLevel = R.id
            WHERE M.id = #{id}
        """
    )
    fun getMemberById(id: Int): Member?

    @Select(
        """
            SELECT 
            M.*,
            MR.regDate AS `extra__regDate`,
            MR.updateDate AS `extra__updateDate`,
            MR.introduce AS `extra__introduce`,
            MR.authenticationLevel AS `extra__authenticationLevel`,
            MR.authenticationDate AS `extra__authenticationDate`,
            MR.roleCategoryId AS `extra__roleCategoryId`,
            R.roleName AS `extra__roleName`
            FROM `member` AS M
            LEFT JOIN memberRole AS MR
            ON M.id = MR.memberId
            LEFT JOIN `role` AS R
            ON M.roleLevel = R.id
            WHERE MR.authenticationLevel = #{authenticationLevel}
            ORDER BY M.id DESC
        """
    )
    fun getMembersByAuthenticationLevel(authenticationLevel: Int): List<Member>?

    @Select(
        """
            SELECT 
            M.*,
            MR.regDate AS `extra__regDate`,
            MR.updateDate AS `extra__updateDate`,
            MR.introduce AS `extra__introduce`,
            MR.authenticationLevel AS `extra__authenticationLevel`,
            MR.authenticationDate AS `extra__authenticationDate`,
            MR.roleCategoryId AS `extra__roleCategoryId`,
            R.roleName AS `extra__roleName`
            FROM `member` AS M
            LEFT JOIN memberRole AS MR
            ON M.id = MR.memberId
            LEFT JOIN `role` AS R
            ON M.roleLevel = R.id
            WHERE M.roleLevel = #{roleLevel}
            ORDER BY M.id DESC
        """
    )
    fun getMembersByRoleLevel(roleLevel: Int): List<Member>?

    @Select(
        """
            SELECT 
            M.*,
            MR.regDate AS `extra__regDate`,
            MR.updateDate AS `extra__updateDate`,
            MR.introduce AS `extra__introduce`,
            MR.authenticationLevel AS `authenticationLevel`,
            MR.authenticationDate AS `extra__authenticationDate`,
            MR.roleCategoryId AS `extra__roleCategoryId`,
            R.roleName AS `extra__roleName`
            FROM `member` AS M
            LEFT JOIN memberRole AS MR
            ON M.id = MR.memberId
            LEFT JOIN `role` AS R
            ON M.roleLevel = R.id
            WHERE M.roleLevel = #{roleLevel}
            AND MR.authenticationLevel = #{authenticationLevel}
            ORDER BY M.id DESC
        """
    )
    fun getMembersByRoleLevelAndAuthenticationLevel(roleLevel: Int, authenticationLevel: Int): List<Member>

    @Insert(
        """
        INSERT INTO `member`
        SET regDate = NOW(),
        updateDate = NOW(),
        loginId = #{loginId},
        loginPw = #{loginPw},
        `name` = #{name},
        cellphoneNo = #{cellphoneNo},
        email = #{email},
        location = #{location},
        bank = #{bank},
        accountNum = #{accountNum}
        """
    )
    fun join(
        loginId: String,
        loginPw: String,
        name: String,
        cellphoneNo: String,
        email: String,
        location: String,
        bank: String,
        accountNum: String
    )
    @Select(
        """
        SELECT R.*
        FROM `role` AS R
        WHERE id != 1
        """
    )
    fun getRoles(): List<Role>

    @Select(
        """
        SELECT D.*
        FROM `department` AS D
        """
    )
    fun getDepartments(): List<Department>

    @Select(
        """
        SELECT B.*
        FROM `bank` AS B
        """
    )
    fun getBanks(): List<Bank>
    // 현재 제단꽃 제작 업체만 존재하기때문에 roleCategoryId 를 임시로 1로 하드코딩
    @Update(
        """
            <script>
            UPDATE memberRole 
            <set>
                authenticationLevel = #{authenticationLevel}, 
                authenticationDate = NOW(),
                <if test="roleLevel == 4">
                roleCategoryId = 1,
                </if>
            </set>
            WHERE memberId = #{memberId}
            </script>
        """
    )
    fun modifyMemberRoleIntoAuthenticationLevelAndRoleLevelByMemberId(memberId: Int, authenticationLevel: Int, roleLevel: Int)

    @Update(
        """
            UPDATE `member` SET 
            roleLevel = #{roleLevel}
            WHERE id = #{memberId};
        """
    )
    fun modifyMemberIntoRoleLevelByMemberId(memberId: Int, roleLevel: Int)

    @Update(
        """
            UPDATE `member` SET 
            requestStatus = #{requestStatus}
            WHERE id = #{memberId}; 
        """
    )
    fun modifyMemberIntoRequestStatusByMemberId(memberId: Int, requestStatus: Boolean)

    @Select(
        """
            SELECT CONCAT(
            SUBSTR(cellphoneNo, 1, 3)
            , '-'
            , SUBSTR(cellphoneNo, 4, 4)
            , '-'
            , SUBSTR(cellphoneNo, 8, 4)
            ) AS `cellphoneNo`
            FROM `member`
            WHERE id = #{id};
        """
    )
    fun getCellphoneNoFormatted(id: Int): String

    @Select(
        """
            SELECT 
            M.*,
            MR.regDate AS `extra__regDate`,
            MR.updateDate AS `extra__updateDate`,
            MR.introduce AS `extra__introduce`,
            MR.authenticationLevel AS `authenticationLevel`,
            MR.authenticationDate AS `extra__authenticationDate`,
            MR.roleCategoryId AS `extra__roleCategoryId`,
            R.roleName AS `extra__roleName`
            FROM `member` AS M
            LEFT JOIN memberRole AS MR
            ON M.id = MR.memberId
            LEFT JOIN `role` AS R
            ON M.roleLevel = R.id
            ORDER BY M.id DESC
        """
    )
    fun getMembers(): List<Member>?


    @Select(
        """
            SELECT LAST_INSERT_ID();
        """
    )
    fun getLastInsertId(): Int

    @Select(
        """
            SELECT 
            M.*,
            MR.regDate AS `extra__regDate`,
            MR.updateDate AS `extra__updateDate`,
            MR.introduce AS `extra__introduce`,
            MR.authenticationLevel AS `extra__authenticationLevel`,
            MR.authenticationDate AS `extra__authenticationDate`,
            MR.roleCategoryId AS `extra__roleCategoryId`,
            R.roleName AS `extra__roleName`
            FROM `member` AS M
            LEFT JOIN memberRole AS MR
            ON M.id = MR.memberId
            LEFT JOIN `role` AS R
            ON M.roleLevel = R.id
            WHERE M.roleLevel = #{roleLevel}
            AND MR.authenticationLevel = #{authenticationLevel}
            ORDER BY M.id DESC
            LIMIT #{itemsInAPage}
            OFFSET #{limitFrom}
        """
    )
    fun getFilteredMembers(roleLevel: Int, authenticationLevel: Int, page: Int, itemsInAPage: Int, limitFrom: Int): List<Member>

    @Select(
        """
            <script>
            SELECT 
            M.*,
            MR.regDate AS `extra__regDate`,
            MR.updateDate AS `extra__updateDate`,
            MR.introduce AS `extra__introduce`,
            MR.authenticationLevel AS `authenticationLevel`,
            MR.authenticationDate AS `extra__authenticationDate`,
            MR.roleCategoryId AS `extra__roleCategoryId`,
            R.roleName AS `extra__roleName`
            FROM `member` AS M
            LEFT JOIN memberRole AS MR
            ON M.id = MR.memberId
            LEFT JOIN `role` AS R
            ON M.roleLevel = R.id
            WHERE M.location = #{location}
            AND M.roleLevel = #{roleLevel}
            AND MR.authenticationLevel = 1
            <if test="roleCategoryId != 0">
            AND MR.roleCategoryId = #{roleCategoryId}
            </if>
            </script>
        """
    )
    fun getMembersByLocationAndRole(location: String, roleLevel: Int, roleCategoryId: Int): List<Member>

    @Update(
        """
            UPDATE `member`
            SET updateDate = NOW(), 
            loginPw = #{loginPwInput}
            WHERE id = #{memberId}
        """
    )
    fun modifyPw(loginPwInput: String, memberId: Int)

    @Update(
        """
            UPDATE `member`
            SET updateDate = NOW(),
            cellphoneNo = #{cellphoneNo},
            email = #{email},
            location = #{location},
            bank = #{bank},
            accountNum = #{accountNum}
            WHERE id = #{id}
        """
    )
    fun modifyInfo(cellphoneNo: String, email: String, location: String, bank: String, accountNum: String, id: Int)

    @Select(
            """
                SELECT 
            M.*,
            MR.regDate AS `extra__regDate`,
            MR.updateDate AS `extra__updateDate`,
            MR.introduce AS `extra__introduce`,
            MR.authenticationLevel AS `authenticationLevel`,
            MR.authenticationDate AS `extra__authenticationDate`,
            MR.roleCategoryId AS `extra__roleCategoryId`,
            R.roleName AS `extra__roleName`
            FROM `member` AS M
            LEFT JOIN memberRole AS MR
            ON M.id = MR.memberId
            LEFT JOIN `role` AS R
            ON M.roleLevel = R.id
            WHERE `name` = #{name}
            AND email = #{email}
            """
    )
    fun getMemberByNameAndEmail(name: String, email: String): Member?
}
