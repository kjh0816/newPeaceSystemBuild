package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.DirectorRepository
import com.base.newPeaceSystemBuild.vo.ResultData
import org.springframework.stereotype.Service

@Service
class DirectorService(
    private val directorRepository: DirectorRepository
) {
    fun putInForDirector(
        loginedMemberId: Int,
        originFileName: String,
        typeCode: String,
        type2Code: String,
        fileExtTypeCode: String,
        fileExtType2Code: String,
        fileExt: String,
        fileNo: Int,
        fileSize: Int,
        fileDir: String
    ) {
        directorRepository.putInForDirector(loginedMemberId, originFileName, typeCode, type2Code, fileExtTypeCode, fileExtType2Code, fileExt, fileNo, fileSize, fileDir)
    }

    fun getLastInsertId(): Int {
        return directorRepository.getLastInsertId()
    }

}
