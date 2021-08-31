package com.base.newPeaceSystemBuild.repository

import com.base.newPeaceSystemBuild.vo.standard.Flower
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update


@Mapper
interface VendorRepository {

    @Select(
    """
        SELECT * 
        FROM FLOWER
    """
    )
    fun getFlowers(): List<Flower>

    @Update(
        """
            UPDATE funeral 
            SET flowerId = #{flowerId} 
            WHERE id = #{funeralId};
        """
    )
    fun modifyFuneralIntoFlowerId(funeralId:Int, flowerId: Int)

}