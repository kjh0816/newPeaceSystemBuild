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
        val funeralHalls = memberRoleRepository.getFuneralHallsByDepartment(department)

        val departmentDetails = mutableListOf<String>()


        // departmentDetail을 담은 후, 중복 제거 (distinct)
        for(funeralHall in funeralHalls){
            departmentDetails.add(funeralHall.departmentDetail)
        }

        // departmentDetail 부분만 따로 리스트에 담은 후, 중복 제거된 상태의 Json 데이터로 넘김
        return ResultData.from("S-1", "시군구를 불러오는데 성공했습니다.", "departmentDetails", departmentDetails.distinct())
    }

}
