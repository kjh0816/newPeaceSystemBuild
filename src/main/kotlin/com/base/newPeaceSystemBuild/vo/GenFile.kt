package com.base.newPeaceSystemBuild.vo

import com.fasterxml.jackson.annotation.JsonIgnore

data class GenFile(
    val id: Int,
    val regDate: String,
    val updateDate: String,
    val delDate: String?,
    val delStatus: Boolean,
    val relTypeCode: String,
    val relId: Int,
    val originFileName: String,
    val fileExt: String,
    val typeCode: String,
    val type2Code: String,
    val fileSize: Int,
    val fileExtTypeCode: String,
    val fileExtType2Code: String,
    val fileNo: Int,
    val fileDir: String
){
    @JsonIgnore
    fun getFilePath(genFileDirPath: String): String? {
        return genFileDirPath + getBaseFileUri()
    }

    @JsonIgnore
    private fun getBaseFileUri(): String {
        return "/" + relTypeCode + "/" + fileDir + "/" + getFileName()
    }

    private fun getFileName(): String {
        return "$id.$fileExt"
    }

    fun getForPrintUrl(): String? {
        return "/gen" + getBaseFileUri() + "?updateDate=" + updateDate
    }
}