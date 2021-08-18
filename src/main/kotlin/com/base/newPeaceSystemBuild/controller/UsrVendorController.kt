package com.base.newPeaceSystemBuild.controller

import com.base.newPeaceSystemBuild.service.VendorService
import com.base.newPeaceSystemBuild.vo.standard.Flower
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class UsrVendorController(
    private val vendorService: VendorService
) {


    @RequestMapping("/usr/vendor/request")
    fun showRequest(model: Model): String{

        val flowers: List<Flower> = vendorService.getFlowers()


        model.addAttribute("flowers", flowers)

        return "usr/vendor/request"
    }
}