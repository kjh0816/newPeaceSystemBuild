package com.base.newPeaceSystemBuild.repository

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper

@Mapper
interface MemberRoleRepository {
    @Insert(
        """
            INSERT INTO memberRole
            SET regDate = NOW(),
            updateDate = NOW(),
            memberId = #{memberId},
            roleId = #{roleId},
            introduce = #{introduce}
        """
    )
    fun putInForDirector(introduce: String, memberId: Int, roleId: Int)
}
