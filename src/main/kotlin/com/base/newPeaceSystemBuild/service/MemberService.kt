package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.MemberRepository
import com.base.newPeaceSystemBuild.vo.Member
import com.base.newPeaceSystemBuild.vo.Role
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun getMemberByLoginId(loginId: String): Member? {
        return memberRepository.getMemberByLoginId(loginId)
    }

    fun getMemberByEmail(email: String): Member? {
        return memberRepository.getMemberByEmail(email)
    }

    fun join(roleLevel: Int, loginId: String, loginPw: String, name: String, cellphoneNo: String, email: String, location: String, profile: String) {
        memberRepository.join(roleLevel, loginId, loginPw, name, cellphoneNo, email, location, profile)
    }

    fun getRoles(): List<Role> {
        return memberRepository.getRoles()
    }
}
