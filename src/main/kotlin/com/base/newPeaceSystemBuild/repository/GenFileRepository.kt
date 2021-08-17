package com.base.newPeaceSystemBuild.repository

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface GenFileRepository {
    @Select(
        """
           SELECT last_insert_id(); 
        """
    )
    fun getLastInsertId(): Int

    @Insert(
        """
            INSERT INTO genFile
            SET regDate = NOW(),
            updateDate = NOW(),
            relTypeCode = #{relTypeCode},
            relId = #{relId},
            typeCode = #{typeCode},
            type2Code = #{type2Code},
            fileNo = #{fileNo},
            originFileName = #{originFileName},
            fileExtTypeCode = #{fileExtTypeCode},
            fileExtType2Code = #{fileExtType2Code},
            fileExt = #{fileExt},
            fileSize = #{fileSize},
            fileDir = #{fileDir}
        """
    )
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
    )


}
