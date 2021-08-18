package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.MemberRepository
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.*
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

    fun isUsableEmail(regex: String, email: String): ResultData {

        if(!Ut.match(regex, email)){
            return ResultData.from("F-1", "이메일 형식에 맞게 입력해주세요.")
        }

        val member = getMemberByEmail(email)
        if(member != null){
            return ResultData.from("F-2", "해당 이메일로 가입된 회원이 이미 존재합니다.")
        }

        return ResultData.from("S-1", "사용 가능한 이메일입니다.")

    }

    fun isUsableLoginId(regex: String, loginId: String): ResultData {
        if(!Ut.match(regex, loginId)){




            return ResultData.from("F-1", "6~20자의 영문 소문자, 숫자만 가능합니다.", "loginId", loginId, "menu", 1)
        }

        val member = getMemberByLoginId(loginId)
        if(member != null){
            return ResultData.from("F-2", loginId+"는(은) 이미 존재하는 로그인 아이디입니다.", "loginId", loginId, "menu", 1)
        }

        return ResultData.from("S-1", "사용 가능한 로그인 아이디입니다.", "loginId", loginId, "menu", 1)
    }
}
