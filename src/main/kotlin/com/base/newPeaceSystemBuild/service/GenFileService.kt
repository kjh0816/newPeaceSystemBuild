package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.GenFileRepository
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.GenFile
import com.base.newPeaceSystemBuild.vo.ResultData
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*


@Service
class GenFileService(
    @Value("\${custom.genFileDirPath}") private val genFileDirPath: String,
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

    ): ResultData {
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


    fun save(multipartFile: MultipartFile, relId: Int): ResultData {
        // VIEW 에 있는 input 박스의 name
        val fileInputName = multipartFile.name
        // 해당 name 을 __ 기준으로 쪼개서 배열에 담음
        val fileInputNameBits = fileInputName.split("__").toTypedArray()

        if (fileInputNameBits[0] != "file") {
            return ResultData.from("F-1", "파라미터 명이 올바르지 않습니다.")
        }

        // input 박스로 데이터가 넘어왔는지 체크하기위한 해당 값의 size
        val fileSize = multipartFile.size.toInt()

        // 사이즈가 0 이거나 음수이면 파일이 업로드되지 않음
        if (fileSize <= 0) {
            return ResultData.from("F-2", "파일이 업로드 되지 않았습니다.")
        }

        // 해당 파일이 어떤 rel 타입인지
        val relTypeCode = fileInputNameBits[1]
        // 해당 파일이 어떤 타입인지
        val typeCode = fileInputNameBits[3]
        // 한번 더 나눈 세부적인 타입
        val type2Code = fileInputNameBits[4]
        // 한번에 여러개의 파일이 업로드되었을 경우 번호를 나눠서 저장하기 위함
        val fileNo = fileInputNameBits[5].toInt()
        // 사용자가 업로드했을 당시의 파일이름
        val originFileName = multipartFile.originalFilename!!
        // 해당 파일이 img 형식인지, video 형식인지 등을 기록함. 추후 img 형식 파일만 받을수 있게 개편해볼떄 사용해보려고 함.
        val fileExtTypeCode: String = Ut.getFileExtTypeCodeFromFileName(multipartFile.originalFilename!!)
        // 해당 파일 확장자타입이 갈려있을경우 하나로 통합해주는 역할. ex ) htm html, jpg jpeg
        val fileExtType2Code: String = Ut.getFileExtType2CodeFromFileName(multipartFile.originalFilename!!)
        // 해당 파일의 실제 확장자
        var fileExt: String = Ut.getFileExtFromFileName(multipartFile.originalFilename!!).lowercase(Locale.getDefault())

        if (fileExt == "jpeg") {
            fileExt = "jpg"
        } else if (fileExt == "htm") {
            fileExt = "html"
        }

        // 리눅스에선 한폴더에 많은 파일을 저장할 수 없어서 월단위로 파일을 끊어서 보관하려고 yml 에서 설정한 파일저장경로 마지막에 현재 Month를 붙혀줌
        val fileDir: String = Ut.getNowYearMonthDateStr()

        val saveMetaRd = saveMeta(
            relTypeCode, relId, typeCode, type2Code, fileNo, originFileName,
            fileExtTypeCode, fileExtType2Code, fileExt, fileSize, fileDir
        )
        // Primary Key 인 ID값
        val newGenFileId = saveMetaRd.getMap()["id"]

        // 새 파일이 저장될 폴더(io파일) 객체 생성
        val targetDirPath: String = "$genFileDirPath/$relTypeCode/$fileDir"
        val targetDir = File(targetDirPath)

        // 새 파일이 저장될 폴더가 존재하지 않는다면 생성
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }

        // yml 경로 + fileDir 합친 해당파일이 저장될 경로
        val targetFileName = "$newGenFileId.$fileExt"
        // 파일이 저장될 경로가 없을경우 생성하기위해 필요해서 선언함
        val targetFilePath = "$targetDirPath/$targetFileName"

        // 파일 생성(업로드된 파일을 지정된 경로롤 옮김)
        multipartFile.transferTo(File(targetFilePath))

        return ResultData.from("S-1", "파일 업로드 성공")
    }

    fun getGenFile(relTypeCode: String, relId: Int, typeCode: String, type2Code: String, fileNo: Int): GenFile? {
        return genFileRepository.getGenFile(relTypeCode, relId, typeCode, type2Code, fileNo);
    }
}
