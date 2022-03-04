package com.snoy.count_down_example.model.repo

import io.reactivex.rxjava3.core.Flowable

interface AuthRepo : Repository {
    fun getSmsOtp(): Flowable<Boolean>
}