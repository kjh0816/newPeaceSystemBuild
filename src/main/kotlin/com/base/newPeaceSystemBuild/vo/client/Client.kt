package com.base.newPeaceSystemBuild.vo.client

data class Client(
    val id: Int,
    val regDate: String,
    val updateDate: String,
    val memberId: Int,
    val directorMemberId: Int,
    val location: String,
    // 고인 관련 정보
    val deceasedName: String,
    val sex: Char,
    // 영업자가 최초에 입력하는 주소(장례지도사 본인이 가까운지 등을 파악하기 위한 용도
    val briefAddress: String,
    // 운구차를 호출할 때 고인이 현재 계신 곳의 주소 - 운구차 모듈에서 입력받는다.
    val deceasedAddress: String,
    // 고인의 주민등록상 주소(장례지도사가 서류상 대조하기 위함) - 장례 정보 수정 및 입력 페이지에서 값을 받는다.
    val deceasedHomeAddress: String,
    val deceasedDate: String,
    val deceasedLunar: Char,
    val frontNum: String,
    val backNum: String,
    val birth: String,
    val birthLunar: Char,
    val funeralHall: String,
    val funeralHallRoom: String,
    val familyClan: String,
    val religion: String,
    val duty: String,
    val funeralMethod: Char,
    val cremationLocation: String,
    val buryLocation: String,
    val cause: Char,
    val papers: Char,
    val autopsyCheck: Char,
    val casketDate: String,
    val casketTime: String,
    val leavingDate: String,
    val leavingTime: String
)
