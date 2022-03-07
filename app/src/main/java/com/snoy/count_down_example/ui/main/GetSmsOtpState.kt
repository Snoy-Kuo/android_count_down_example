package com.snoy.count_down_example.ui.main

sealed interface GetSmsOtpState {
    object GetSmsOtpInitial : GetSmsOtpState
    object GetSmsOtpLoading : GetSmsOtpState
    object GetSmsOtpSuccess : GetSmsOtpState
    data class GetSmsOtpWaiting(val secs: Long) : GetSmsOtpState
    data class GetSmsOtpFail(val msg: String) : GetSmsOtpState
}