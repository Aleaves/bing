package com.app.smartcode.exp

open class ApiException(val errorCode: Int, private val mErrorMsg: String?) : RuntimeException(mErrorMsg) {

    val errorMsg: String?
        get() = if (mErrorMsg != null && mErrorMsg.startsWith("HTTP")) "未知错误" else mErrorMsg
}