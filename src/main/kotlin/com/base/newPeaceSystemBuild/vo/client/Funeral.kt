package com.base.newPeaceSystemBuild.vo.client


// Funeral 테이블은 장례지도사가 매칭되었을 때 데이터가 들어간다.
data class Funeral(
    val id: Int,
    val regDate: String,
    val updateDate: String,
//  고인 정보를 담는 테이블의 id
    val clientId: Int,
//  directorMemberId = 장례를 진행하는 장례지도사
    val directorMemberId: Int,
//  memberId = 영업자(장례지도사를 연결해준 member)
    val memberId: Int,
//  flowerId = 어떤 꽃을 선택했는지
    val flowerId: Int,
//  portraitId = 어떤 영정을 선택했는지
// progress = 장례 진행 상태(장례지도사 연결 여부 ~ 장례식 종료) default = 0(장례지도사 연결 안 됨)
    val progress: Int
)
