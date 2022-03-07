package com.snoy.count_down_example.model.repo

import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

class FakeAuthRepo : AuthRepo {

    companion object {
        var GET_OTP_RESP: Boolean = true
    }

    override fun getSmsOtp(): Flowable<Boolean> {
        return Flowable.just(GET_OTP_RESP)
            .delay(2, TimeUnit.SECONDS)
    }

    override fun getSmsOtp2(): Flow<Boolean> {
        return flow {
            delay(2000L)
            emit(GET_OTP_RESP)
        }
    }

    fun setResp(success:Boolean){
        GET_OTP_RESP = success
    }
}