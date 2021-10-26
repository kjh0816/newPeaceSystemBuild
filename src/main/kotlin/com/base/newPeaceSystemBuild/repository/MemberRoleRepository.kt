package com.base.newPeaceSystemBuild.repository

import com.base.newPeaceSystemBuild.vo.client.Family
import com.base.newPeaceSystemBuild.vo.client.FuneralHall
import com.base.newPeaceSystemBuild.vo.standard.Coffin
import groovy.time.BaseDuration
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

    @Select(
            """
                SELECT DISTINCT(chi)
                FROM coffin
                WHERE name = #{coffinName}
            """
    )
    fun getCoffinChisByName(coffinName: String): List<String>

    @Select(
            """
                SELECT DISTINCT(size)
                FROM coffin
                WHERE chi = #{coffinChi}
            """
    )
    fun getCoffinSizesByChi(coffinChi: String): List<String>

    @Select(
            """
                SELECT *
                FROM coffin
                WHERE name = #{coffinName}
                AND chi = #{coffinChi}
                AND size = #{coffinSize}
            """
    )
    fun getCoffinByAll(coffinName: String, coffinChi: String, coffinSize: String): Coffin

    @Select(
            """
                SELECT *
                FROM funeralHall
                WHERE address = #{destinationAddress}
            """
    )
    fun getFuneralHallByAddress(destinationAddress: String): FuneralHall

    @Select(
            """
                SELECT DISTINCT(departmentDetail)
                FROM funeralHall
                WHERE department = #{department}
            """
    )
    fun getDepartmentDetailsByDepartment(department: String): List<String>

    @Select(
            """
                SELECT DISTINCT(name)
                FROM funeralHall
                WHERE departmentDetail = #{departmentDetail}
            """
    )
    fun getFuneralHallNamesByDepartmentDetail(departmentDetail: String): List<String>

    @Insert(
            """
                INSERT INTO helper
                SET regDate = NOW(),
                updateDate = NOW(),
                funeralId = #{funeralId},
                department = #{department},
                workDate = #{workDate},
                workStartTime = #{workStartTime},
                workFinishTime = #{workFinishTime}
            """
    )
    fun insertIntoHelper(funeralId: Int, department: String, workDate: String, workStartTime: String, workFinishTime: String)

    @Select(
            """
            SELECT LAST_INSERT_ID();
        """
    )
    fun getLastInsertId(): Int


}
