package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.MemberRoleRepository
import org.springframework.stereotype.Service


@Service
class MemberRoleService(
    private val memberRoleRepository: MemberRoleRepository
) {


    fun insertMemberRole(introduce: String, memberId: Int, roleId: Int) {
        memberRoleRepository.insertMemberRole(introduce, memberId, roleId)
    }

}
