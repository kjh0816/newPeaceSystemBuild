package com.base.newPeaceSystemBuild.vo

import com.base.newPeaceSystemBuild.interceptor.BeforeActionInterceptor
import com.base.newPeaceSystemBuild.interceptor.NeedLoginInterceptor
import com.base.newPeaceSystemBuild.interceptor.NeedLogoutInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class MyWebMvcConfigurer(
    private val beforeActionInterceptor: BeforeActionInterceptor,
    private val needLoginInterceptor: NeedLoginInterceptor,
    private val needLogoutInterceptor: NeedLogoutInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(beforeActionInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/resource/**")
            .excludePathPatterns("/error")
        registry.addInterceptor(needLoginInterceptor)
            .addPathPatterns("/usr/account/home")
        registry.addInterceptor(needLogoutInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/usr/member/login")
            .excludePathPatterns("/usr/member/doLogin")
            .excludePathPatterns("/usr/member/join")
            .excludePathPatterns("/usr/member/doJoin")
    }
}