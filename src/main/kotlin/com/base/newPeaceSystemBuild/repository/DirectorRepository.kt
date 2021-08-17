package com.base.newPeaceSystemBuild.repository

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface DirectorRepository {
    @Select(
        """
           SELECT last_insert_id(); 
        """
    )
    fun getLastInsertId(): Int
    @Insert(
        """
            <script>
            INSERT INTO director
            SET regDate = NOW(),
            updateDate = NOW(),
            memberId = #{loginedMemberId},
            <if test="aboutMe.trim() != ''">
                 aboutMe = #{aboutMe},
            </if>
            originFileName = #{originFileName},
            typeCode = #{typeCode},
            type2Code = #{type2Code},
            fileExtTypeCode = #{fileExtTypeCode},
            fileExtType2Code = #{fileExtType2Code},
            fileSize = #{fileSize},
            fileExt = #{fileExt},
            fileNo = #{fileNo},
            fileDir = #{fileDir}
            </script>
        """
    )
    fun putInForDirector(
        loginedMemberId: Int,
        aboutMe: String,
        originFileName: String,
        typeCode: String,
        type2Code: String,
        fileExtTypeCode: String,
        fileExtType2Code: String,
        fileExt: String,
        fileNo: Int,
        fileSize: Int,
        fileDir: String
    )

}
