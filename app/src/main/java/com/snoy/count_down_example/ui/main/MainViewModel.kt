package com.snoy.count_down_example.ui.main

import androidx.lifecycle.ViewModel
import com.snoy.count_down_example.model.repo.AuthRepo
import com.snoy.count_down_example.utils.tickerFlow
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit

class MainViewModel(private val repo: AuthRepo) : ViewModel() {

    fun getSmsOTP(countdownSecs: Long): Flowable<Long> {
        //start from 0, emit 11 numbers：0-10，with init delay 0 sec, emit one element per second.
        return repo.getSmsOtp()
            .flatMap { success ->
                return@flatMap if (success) {
                    getSmsOtpDelay(countdownSecs)
                } else {
                    Flowable.just(-1)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getSmsOtpDelay(countdownSecs: Long): Flowable<Long> {
        //start from 0, emit 11 numbers：0-10，with init delay 0 sec, emit one element per second.
        return Flowable.intervalRange(0, countdownSecs + 1, 0, 1, TimeUnit.SECONDS)
    }

    @OptIn(FlowPreview::class)
    fun getSmsOTP2(countdownSecs: Long): Flow<Long> {
        //start from 0, emit 11 numbers：0-10，with init delay 0 sec, emit one element per second.
        return repo.getSmsOtp2()
            .flatMapMerge { success ->
                return@flatMapMerge if (success) {
                    getSmsOtpDelay2(countdownSecs)
                } else {
                    flowOf(-1L)
                }
            }
            .flowOn(Dispatchers.IO) // Works upstream, doesn't change downstream
            .flowOn(Dispatchers.Main)
    }

    private fun getSmsOtpDelay2(countdownSecs: Long): Flow<Long> {
        //start from countdownSecs to 0，emit one element per second.
        return tickerFlow(countdownSecs, 0)
    }
}