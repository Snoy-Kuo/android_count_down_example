package com.snoy.count_down_example.model.repo

import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow

interface AuthRepo : Repository {
    fun getSmsOtp(): Flowable<Boolean>

    fun getSmsOtp2(): Flow<Boolean>
}