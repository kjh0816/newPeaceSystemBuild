package com.base.newPeaceSystemBuild.vo

import com.base.newPeaceSystemBuild.interceptor.BeforeActionInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class MyWebMvcConfigurer(private val beforeActionInterceptor: BeforeActionInterceptor) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(beforeActionInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/resource/**")
            .excludePathPatterns("/error")
    }
}