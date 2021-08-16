package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.DirectorRepository
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.ResultData
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.util.*


@Service
class DirectorService(
    @Value("\${custom.directorFileDirPath}") private val directorFileDirPath: String,
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
    ): ResultData<Any> {
        directorRepository.putInForDirector(loginedMemberId, originFileName, typeCode, type2Code, fileExtTypeCode, fileExtType2Code, fileExt, fileNo, fileSize, fileDir)

        return ResultData.from("S-1", "파일 업로드에 성공하였습니다.", "id", getLastInsertId())
    }

    fun getLastInsertId(): Int {
        return directorRepository.getLastInsertId()
    }

    fun save(multipartFile: MultipartFile, loginedMemberId: Int): ResultData<Any> {
        val fileInputName = multipartFile.name

        val fileInputNameBits: List<String> = fileInputName.split("__")

        if(fileInputNameBits[0] != "file"){
            ResultData.from("F-1", "파라미터 명이 올바르지 않습니다.")
        }

        val fileSize = multipartFile!!.size.toInt()

        if (fileSize <= 0) {
            ResultData.from("F-2", "파일이 업로드되지 않았습니다.")
        }

        val relId: Int = loginedMemberId
        val fileNo = fileInputNameBits[5].toInt()
        val originFileName = multipartFile.originalFilename!!
        val typeCode = fileInputNameBits[3]
        val type2Code = fileInputNameBits[4]
        val fileExtTypeCode: String = Ut.getFileExtTypeCodeFromFileName(multipartFile.originalFilename!!)
        val fileExtType2Code: String = Ut.getFileExtType2CodeFromFileName(multipartFile.originalFilename!!)
        var fileExt: String = Ut.getFileExtFromFileName(multipartFile.originalFilename!!).lowercase(Locale.getDefault())
        val fileDir: String = Ut.getNowYearMonthDateStr()

        if (fileExt == "jpeg") {
            fileExt = "jpg";
        } else if (fileExt == "htm") {
            fileExt = "html";
        }

        val metaDataRd: ResultData<Any> = putInForDirector(relId, originFileName, typeCode, type2Code, fileExtTypeCode, fileExtType2Code, fileExt, fileNo, fileSize, fileDir)

        val directorId = metaDataRd.getData()

        val targetDirPath: String = "$directorFileDirPath/director/$fileDir"
        val targetDir = File(targetDirPath)

        // 새 파일이 저장될 폴더가 존재하지 않는다면 생성
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        // 서버에 저장될 파일이름
        val targetFileName: String = "$directorId.$fileExt"
        // 해당 파일의 경로포함 파일이름에 확장자까지 풀네임
        val targetFilePath = "$targetDirPath/$targetFileName"

        // 파일 생성(업로드된 파일을 지정된 경로롤 옮김)
        multipartFile.transferTo(File(targetFilePath))

        return ResultData.from("S-1", "파일이 생성되었습니다.", "directorId", directorId)
    }

}
