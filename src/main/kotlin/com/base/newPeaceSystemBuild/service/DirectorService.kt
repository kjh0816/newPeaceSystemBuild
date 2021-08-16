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

        // relId 로 표현했지만 memberId 칼럼값이다. 추후 다른 파일업로드 기능때 헷갈리지 않기위해 변수명은 relId로 했다.
        val relId: Int = loginedMemberId
        // 파일 번호인데, 현재 director 에서는 파일을 한개만 받기때문에, fileNo가 1밖에없지만 여러개의 파일 받을경우를 위해 남겨둠.
        val fileNo = fileInputNameBits[5].toInt()
        // 실제 사용자가 업로드한 파일의 이름.
        val originFileName = multipartFile.originalFilename!!
        // input name값을 쪼개서 넣어준건데, 가변값도 아니고 고정값이라 왜 필요한진 잘 모르겠음. 관례라고 해서 넣어둠.
        val typeCode = fileInputNameBits[3]
        // 마찬가지
        val type2Code = fileInputNameBits[4]
        // 해당 파일이 img 형식인지, video 형식인지 등을 기록함. 추후 img 형식 파일만 받을수 있게 개편해볼떄 사용해보려고 함.
        val fileExtTypeCode: String = Ut.getFileExtTypeCodeFromFileName(multipartFile.originalFilename!!)
        // 해당 파일 확장자타입이 갈려있을경우 하나로 통합해주는 역할. / 왜 있는지는 잘 모르겠어서 뺄까 고민중
        val fileExtType2Code: String = Ut.getFileExtType2CodeFromFileName(multipartFile.originalFilename!!)
        // 해당 파일의 실제 확장자 명
        var fileExt: String = Ut.getFileExtFromFileName(multipartFile.originalFilename!!).lowercase(Locale.getDefault())
        // 서브 디렉토리 같은느낌 yml에서 설정해준 경로에 추가적으로 이걸 붙혀서 관리함. (날짜 정보) 리눅스에서 폴더 하나에 너무 많은파일은 저장하지 못하기에 분리.
        val fileDir: String = Ut.getNowYearMonthDateStr()

        if (fileExt == "jpeg") {
            fileExt = "jpg";
        } else if (fileExt == "htm") {
            fileExt = "html";
        }

        // DB에 파일의 메타정보 저장
        val metaDataRd: ResultData<Any> = putInForDirector(relId, originFileName, typeCode, type2Code, fileExtTypeCode, fileExtType2Code, fileExt, fileNo, fileSize, fileDir)

        // Primary Key 값인 id 값
        val directorId = metaDataRd.getData()

        // yml 경로 + fileDir 합친 해당파일이 저장될 경로
        val targetDirPath: String = "$directorFileDirPath/director/$fileDir"
        // 파일이 저장될 경로가 없을경우 생성하기위해 필요해서 선언함
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
