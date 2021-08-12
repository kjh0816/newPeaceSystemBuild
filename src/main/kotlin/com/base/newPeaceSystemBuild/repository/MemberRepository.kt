package com.base.newPeaceSystemBuild.repository

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
        roleLevel = #{roleLevel},
        loginId = #{loginId},
        loginPw = #{loginPw},
        `name` = #{name},
        cellphoneNo = #{cellphoneNo},
        email = #{email},
        location = #{location},
        `profile` = #{profile}
        """
    )
    fun join(roleLevel: Int, loginId: String, loginPw: String, name: String, cellphoneNo: String, email: String, location: String, profile: String)
    @Select(
        """
        SELECT *
        FROM `role` AS R
        WHERE id != 1
        """
    )
    fun getRoles(): List<Role>
}
