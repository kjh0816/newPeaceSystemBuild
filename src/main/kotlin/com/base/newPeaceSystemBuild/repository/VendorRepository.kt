package com.base.newPeaceSystemBuild.repository

import com.base.newPeaceSystemBuild.vo.standard.Flower
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select


@Mapper
interface VendorRepository {

    @Select(
    """
        SELECT * 
        FROM FLOWER
    """
    )
    fun getFlowers(): List<Flower>

}