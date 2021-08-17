package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.GenFileRepository
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.ResultData
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.util.*


@Service
class GenFileService(
    @Value("\${custom.directorFileDirPath}") private val genFileDirPath: String,
    private val genFileRepository: GenFileRepository
) {
    fun saveMeta(
        relTypeCode: String,
        relId: Int,
        typeCode: String,
        type2Code: String,
        fileNo: Int,
        originFileName: String,
        fileExtTypeCode: String,
        fileExtType2Code: String,
        fileExt: String,
        fileSize: Int,
        fileDir: String

    ): ResultData<Int> {
        genFileRepository.saveMeta(
            relTypeCode,
            relId,
            typeCode,
            type2Code,
            fileNo,
            originFileName,
            fileExtTypeCode,
            fileExtType2Code,
            fileExt,
            fileSize,
            fileDir
        )
        return ResultData.from("S-1", "파일정보 업로드에 성공하였습니다.", "id", getLastInsertId())
    }

    fun getLastInsertId(): Int {
        return genFileRepository.getLastInsertId()
    }


    fun save(multipartFile: MultipartFile, relId: Int): ResultData<String> {
        val fileInputName = multipartFile.name
        val fileInputNameBits = fileInputName.split("__").toTypedArray()

        if (fileInputNameBits[0] != "file") {
            return ResultData.from("F-1", "파라미터 명이 올바르지 않습니다.")
        }

        val fileSize = multipartFile.size.toInt()

        if (fileSize <= 0) {
            return ResultData.from("F-2", "파일이 업로드 되지 않았습니다.")
        }

        val relTypeCode = fileInputNameBits[1]
        val typeCode = fileInputNameBits[3]
        val type2Code = fileInputNameBits[4]
        val fileNo = fileInputNameBits[5].toInt()
        val originFileName = multipartFile.originalFilename!!
        val fileExtTypeCode: String = Ut.getFileExtTypeCodeFromFileName(multipartFile.originalFilename!!)
        val fileExtType2Code: String = Ut.getFileExtType2CodeFromFileName(multipartFile.originalFilename!!)
        var fileExt: String = Ut.getFileExtFromFileName(multipartFile.originalFilename!!).lowercase(Locale.getDefault())

        if (fileExt == "jpeg") {
            fileExt = "jpg"
        } else if (fileExt == "htm") {
            fileExt = "html"
        }

        val fileDir: String = Ut.getNowYearMonthDateStr()

        val saveMetaRd = saveMeta(
            relTypeCode, relId, typeCode, type2Code, fileNo, originFileName,
            fileExtTypeCode, fileExtType2Code, fileExt, fileSize, fileDir
        )
        val newGenFileId = saveMetaRd.getData()

        // 새 파일이 저장될 폴더(io파일) 객체 생성
        val targetDirPath: String = "$genFileDirPath/$relTypeCode/$fileDir"
        val targetDir = File(targetDirPath)

        // 새 파일이 저장될 폴더가 존재하지 않는다면 생성
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }

        val targetFileName = "$newGenFileId.$fileExt"
        val targetFilePath = "$targetDirPath/$targetFileName"

        // 파일 생성(업로드된 파일을 지정된 경로롤 옮김)
        multipartFile.transferTo(File(targetFilePath))

        return ResultData.from("S-1", "파일 업로드 성공")
    }
}
