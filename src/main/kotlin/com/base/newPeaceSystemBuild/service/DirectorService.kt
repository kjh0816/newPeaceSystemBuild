package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.DirectorRepository
import com.base.newPeaceSystemBuild.vo.ResultData
import org.springframework.stereotype.Service

@Service
class DirectorService(
    private val genFileRepository: DirectorRepository
) {
    fun putInForDirector(
        loginedMemberId: Int,
        originFileName: String?,
        fileExtTypeCode: String,
        fileExtType2Code: String,
        fileExt: String,
        fileDir: Int,
        fileDir1: String
    ): ResultData {

    }

}
