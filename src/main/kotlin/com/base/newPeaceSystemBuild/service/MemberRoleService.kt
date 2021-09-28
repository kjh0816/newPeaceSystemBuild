package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.MemberRoleRepository
import com.base.newPeaceSystemBuild.vo.ResultData
import com.base.newPeaceSystemBuild.vo.client.FuneralHall
import org.springframework.stereotype.Service


@Service
class MemberRoleService(
    private val memberRoleRepository: MemberRoleRepository
) {


    fun insertMemberRole(introduce: String, memberId: Int, roleId: Int, roleCategoryId: Int) {
        memberRoleRepository.insertMemberRole(introduce, memberId, roleId, roleCategoryId)
    }

    fun getFuneralHallsByDepartment(department: String): ResultData {
        val departmentDetails = memberRoleRepository.getFuneralHallsByDepartment(department)

        return ResultData.from("S-1", "시군구를 불러오는데 성공했습니다.", "departmentDetails", departmentDetails)
    }

}
