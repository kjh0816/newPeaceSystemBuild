package com.base.newPeaceSystemBuild.repository

import com.base.newPeaceSystemBuild.vo.Bank
import com.base.newPeaceSystemBuild.vo.Department
import com.base.newPeaceSystemBuild.vo.Member
import com.base.newPeaceSystemBuild.vo.Role
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface MemberRepository {
    @Select(
        """
        SELECT *
        FROM `member` AS M
        WHERE M.loginId = #{loginId}
        """
    )
    fun getMemberByLoginId(@Param("loginId") loginId: String): Member?
    @Select(
        """
        SELECT *
        FROM `member` AS M
        WHERE M.email = #{email}
        """
    )
    fun getMemberByEmail(email: String): Member?

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
}
