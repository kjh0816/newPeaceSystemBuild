package com.base.newPeaceSystemBuild.repository

import com.base.newPeaceSystemBuild.vo.client.Family
import com.base.newPeaceSystemBuild.vo.client.FuneralHall
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface MemberRoleRepository {

    @Insert(
        """
            <script>
            INSERT INTO memberRole
            <set>
            regDate = NOW(),
            updateDate = NOW(),
            memberId = #{memberId},
            roleId = #{roleId},
            introduce = #{introduce},
            <if test="roleId == 4">
            roleCategoryId = #{roleCategoryId},
            </if>
            </set>
            </script>
        """
    )
    fun insertMemberRole(introduce: String, memberId: Int, roleId: Int, roleCategoryId: Int)

    @Select(
            """
                SELECT *
                FROM funeralHall
                WHERE department = #{department}
            """
    )
    fun getFuneralHallsByDepartment(department: String): List<FuneralHall>


    @Select(
            """
                SELECT *
                FROM funeralHall
                WHERE departmentDetail = #{departmentDetail}
            """
    )
    fun getFuneralHallsByDepartmentDetail(departmentDetail: String): List<FuneralHall>
    @Select(
            """
                SELECT *
                FROM funeralHall
                WHERE name = #{name}
            """
    )
    fun getFuneralHallByName(name: String): FuneralHall


}
