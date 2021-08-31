package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.VendorRepository
import com.base.newPeaceSystemBuild.vo.standard.Flower
import org.springframework.stereotype.Service


@Service
class VendorService(
    private val vendorRepository: VendorRepository
) {
    fun getFlowers(): List<Flower> {
        // 취급할 제단꽃 standard를 불러온다.
        return vendorRepository.getFlowers()
    }

    fun modifyFuneralIntoFlowerId(funeralId:Int, flowerId: Int) {
        vendorRepository.modifyFuneralIntoFlowerId(funeralId, flowerId)
    }
}