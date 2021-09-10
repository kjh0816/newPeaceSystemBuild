package com.base.newPeaceSystemBuild

import com.base.newPeaceSystemBuild.interceptor.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebMvcConfigurer(
    @Value("\${custom.genFileDirPath}") private val genFileDirPath: String,
    private val beforeActionInterceptor: BeforeActionInterceptor,
    private val needLoginInterceptor: NeedLoginInterceptor,
    private val needLogoutInterceptor: NeedLogoutInterceptor,
    private val needAdminInterceptor: NeedAdminInterceptor,
    private val requestInterceptor: RequestInterceptor,
    private val directorInterceptor: DirectorInterceptor,
    private val vendorInterceptor: VendorInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(beforeActionInterceptor)
            .addPathPatterns("/**") // 모든 template view에 접근 가능
            .excludePathPatterns("/resource/**") // resource 하위 폴더 및 파일은 제외
            .excludePathPatterns("/peace/**") // 업로드된 파일이 저장되는 폴더
            .excludePathPatterns("/error")
        registry.addInterceptor(needLoginInterceptor)
            //          화이트 리스트 방식
            .addPathPatterns("/adm/**")
            .addPathPatterns("/usr/member/**")
            .addPathPatterns("/usr/director/**")
            .addPathPatterns("/usr/vendor/**")
            .excludePathPatterns("/usr/member/login")
            .excludePathPatterns("/usr/member/doLogin")
            .excludePathPatterns("/usr/member/join")
            .excludePathPatterns("/usr/member/doJoin")
            .excludePathPatterns("/usr/member/loginIdCheck")
            .excludePathPatterns("/usr/member/emailCheck")
            .excludePathPatterns("/usr/member/findId")
            .excludePathPatterns("/usr/member/doFindId")
            .excludePathPatterns("/usr/member/findPw")
            .excludePathPatterns("/usr/member/doFindPw")
        registry.addInterceptor(needAdminInterceptor)
            //          화이트 리스트 방식
            .addPathPatterns("/adm/**")
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
        registry.addInterceptor(vendorInterceptor)
            //          화이트 리스트 방식
            .addPathPatterns("/usr/vendor/**")
            .excludePathPatterns("/usr/vendor/request")
            .excludePathPatterns("/usr/vendor/doRequest")
            .excludePathPatterns("/usr/vendor/portraitRequest")
            .excludePathPatterns("/usr/vendor/doPortraitRequest")
            .excludePathPatterns("/usr/vendor/explain")
        registry.addInterceptor(requestInterceptor)
            //          화이트 리스트 방식
            .addPathPatterns("/usr/director/request")
            .addPathPatterns("/usr/director/doRequest")
            .addPathPatterns("/usr/vendor/request")
            .addPathPatterns("/usr/vendor/doRequest")
            .addPathPatterns("/usr/vendor/portraitRequest")
            .addPathPatterns("/usr/vendor/doPortraitRequest")
    }

    // 정적 리소스 로드
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/peace/**")
            .addResourceLocations("file:/$genFileDirPath/")
            .setCachePeriod(20)
    }
}