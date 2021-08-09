package com.base.newPeaceSystemBuild.vo

data class Member (
        private val id: Int,

        private val regDate: String,
        private val updateDate: String,

        private val roleLevel: Int,

        private val loginId: String,
        private val loginPw: String,

        private val name: String,
        private val cellphoneNo: String,
        private val email: String,

        private val location: String,
        private val profile: String,

        private val delStatus: Boolean,
        private val delDate: String
        )