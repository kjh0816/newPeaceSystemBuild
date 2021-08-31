package com.base.newPeaceSystemBuild.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Aligo__send__ResponseBody(
        val result_code: String?,
        val message: String?,
        val msg_id: String?,
        val success_cnt: Int?,
        val error_cnt: Int?,
        val msg_type: String?
)