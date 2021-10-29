package com.base.newPeaceSystemBuild.vo.worker

data class Helper(
        val id: Int,
        val regDate: String,
        val updateDate: String,
        val funeralId: Int,
        val helperMemberId: Int,
        val helperMemberCount: Int,
        val helperPackageId: Int,
        val department: String,
        val workDate: String,
        val workStartTime: String,
        val workFinishTime: String,
        val additionalWorkHour: Int,
        val pay: String,
        val additionalPay: String,
        val completionStatus: Boolean
)
