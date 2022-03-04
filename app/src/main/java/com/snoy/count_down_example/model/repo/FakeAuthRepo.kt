package com.snoy.count_down_example.model.repo

import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.TimeUnit

class FakeAuthRepo : AuthRepo {

    override fun getSmsOtp(): Flowable<Boolean> {
        return Flowable.just(true)
            .delay(2, TimeUnit.SECONDS)
    }
}