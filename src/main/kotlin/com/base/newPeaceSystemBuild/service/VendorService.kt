package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.VendorRepository
import com.base.newPeaceSystemBuild.vo.standard.Flower
import org.springframework.stereotype.Service


@Service
class VendorService(
    private val vendorRepository: VendorRepository
) {
    fun getFlowers(): List<Flower> {
        return vendorRepository.getFlowers()
    }
}