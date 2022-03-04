package com.snoy.count_down_example.ui.main

import androidx.lifecycle.ViewModel
import com.snoy.count_down_example.model.repo.AuthRepo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel(private val repo: AuthRepo) : ViewModel() {

    fun getSmsOTP(countdownSecs: Long): Flowable<Long> {
        //start from 0, emit 11 numbers：0-10，with init delay 0 sec, emit one element per second.
        return repo.getSmsOtp()
            .flatMap { success ->
                return@flatMap if (success) {
                    getSmsOtpDelay(countdownSecs)
                } else {
                    Flowable.just(0)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getSmsOtpDelay(countdownSecs: Long): Flowable<Long> {
        //start from 0, emit 11 numbers：0-10，with init delay 0 sec, emit one element per second.
        return Flowable.intervalRange(0, countdownSecs + 1, 0, 1, TimeUnit.SECONDS)
    }
}