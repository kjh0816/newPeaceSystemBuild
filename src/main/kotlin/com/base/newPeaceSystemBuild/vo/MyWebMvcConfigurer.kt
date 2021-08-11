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
            .addPathPatterns("/usr/member/doLogout")
        registry.addInterceptor(needLogoutInterceptor)
            .addPathPatterns("/usr/member/login")
            .addPathPatterns("/usr/member/doLogin")
            .addPathPatterns("/usr/member/join")
            .addPathPatterns("/usr/member/doJoin")
    }
}