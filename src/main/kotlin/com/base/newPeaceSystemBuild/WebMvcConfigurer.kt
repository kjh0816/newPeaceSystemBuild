package com.base.newPeaceSystemBuild

import com.base.newPeaceSystemBuild.interceptor.BeforeActionInterceptor
import com.base.newPeaceSystemBuild.interceptor.DirectorInterceptor
import com.base.newPeaceSystemBuild.interceptor.NeedLoginInterceptor
import com.base.newPeaceSystemBuild.interceptor.NeedLogoutInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfigurer(
    private val beforeActionInterceptor: BeforeActionInterceptor,
    private val needLoginInterceptor: NeedLoginInterceptor,
    private val needLogoutInterceptor: NeedLogoutInterceptor,
    private val directorInterceptor: DirectorInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(beforeActionInterceptor)
            .addPathPatterns("/**") // 모든 template view에 접근 가능
            .excludePathPatterns("/resource/**") // resource 하위 폴더 및 파일은 제외
            .excludePathPatterns("/error")
        registry.addInterceptor(needLoginInterceptor)
            //          화이트 리스트 방식
            .addPathPatterns("/usr/member/**")
            .addPathPatterns("/usr/director/**")
            .excludePathPatterns("/usr/member/login")
            .excludePathPatterns("/usr/member/doLogin")
            .excludePathPatterns("/usr/member/join")
            .excludePathPatterns("/usr/member/doJoin")
        registry.addInterceptor(needLogoutInterceptor)
            //          블랙 리스트 방식
            .addPathPatterns("/usr/member/login")
            .addPathPatterns("/usr/member/doLogin")
            .addPathPatterns("/usr/member/join")
            .addPathPatterns("/usr/member/doJoin")
        registry.addInterceptor(directorInterceptor)
            //          화이트 리스트 방식
            .addPathPatterns("/usr/director/**")
            .excludePathPatterns("/usr/director/request")
            .excludePathPatterns("/usr/director/doRequest")
    }
}