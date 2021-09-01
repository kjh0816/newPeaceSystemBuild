package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.VendorRepository
import com.base.newPeaceSystemBuild.vo.ResultData
import com.base.newPeaceSystemBuild.vo.client.Funeral
import com.base.newPeaceSystemBuild.vo.standard.Flower
import org.mybatis.spring.MyBatisSystemException
import org.springframework.stereotype.Service


@Service
class VendorService(
    private val vendorRepository: VendorRepository
) {
    fun getFlowers(): List<Flower> {
        // 취급할 제단꽃 standard를 불러온다.
        return vendorRepository.getFlowers()
    }

    fun getFuneralById(funeralId: Int): Funeral{
        return vendorRepository.getFuneralById(funeralId)
    }

    fun modifyFuneralIntoFlowerId(funeralId:Int, flowerId: Int): ResultData {
        vendorRepository.modifyFuneralIntoFlowerId(funeralId, flowerId)
        val funeral = getFuneralById(funeralId)

        return ResultData.from("S-1", "제단꽃 선택이 완료되었습니다.", "funeral", funeral)
    }

    fun getFlowerById(flowerId: Int): Flower {
        return vendorRepository.getFlowerById(flowerId)
    }
}