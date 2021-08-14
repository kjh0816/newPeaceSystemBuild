package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.MemberRepository
import com.base.newPeaceSystemBuild.vo.Bank
import com.base.newPeaceSystemBuild.vo.Department
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

    fun join(
        loginId: String,
        loginPw: String,
        name: String,
        cellphoneNo: String,
        email: String,
        location: String,
        bank: String,
        accountNum: String
    ) {
        memberRepository.join(loginId, loginPw, name, cellphoneNo, email, location, bank, accountNum)
    }

    fun getRoles(): List<Role> {
        return memberRepository.getRoles()
    }

    fun getDepartments(): List<Department> {
        return memberRepository.getDepartments()
    }

    fun getBanks(): List<Bank> {
        return memberRepository.getBanks()
    }
}
