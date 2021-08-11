package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.MemberRepository
import com.base.newPeaceSystemBuild.vo.Member
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun getMemberByLoginId(loginId: String): Member? {
        return memberRepository.getMemberByLoginId(loginId)
    }
}
