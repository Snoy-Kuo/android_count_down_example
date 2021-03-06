package com.snoy.count_down_example.ui.main

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snoy.count_down_example.model.repo.AuthRepo
import com.snoy.count_down_example.model.repo.FakeAuthRepo
import com.snoy.count_down_example.utils.tickerFlow
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainViewModel(private val repo: AuthRepo) : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()

    val getSmsOtp3State: MutableLiveData<GetSmsOtpState> by lazy {
        MutableLiveData(GetSmsOtpState.GetSmsOtpInitial)
    }
    val getSmsOtp4State: MutableLiveData<GetSmsOtpState> by lazy {
        MutableLiveData(GetSmsOtpState.GetSmsOtpInitial)
    }

    val getSmsOtp5State: MutableStateFlow<GetSmsOtpState> by lazy {
        MutableStateFlow(GetSmsOtpState.GetSmsOtpInitial)
    }

    val getSmsOtp6State: BehaviorSubject<GetSmsOtpState> by lazy {
        BehaviorSubject.createDefault(GetSmsOtpState.GetSmsOtpInitial)
    }

    //RxJava
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

    //Flow
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

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    //RxJava to LiveData
    fun getSmsOTP3(countdownSecs: Long) {
        getSmsOtp3State.value = GetSmsOtpState.GetSmsOtpLoading
        val disposable = repo.getSmsOtp()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { success ->
                if (success) {
                    getSmsOtp3State.value = GetSmsOtpState.GetSmsOtpSuccess
                    getSmsOtpDelay3(countdownSecs, callback = { secs ->
                        getSmsOtp3State.value = secsToSmsOtpState(secs)
                    })
                } else {
                    getSmsOtp3State.value = GetSmsOtpState.GetSmsOtpFail("FAIL")
                }
            }
        disposables.add(disposable)
    }

    private fun getSmsOtpDelay3(countdownSecs: Long, callback: (secs: Long) -> Unit) {
        object : CountDownTimer(countdownSecs * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                //seconds remaining
                callback(millisUntilFinished / 1000 + 1)
            }

            override fun onFinish() {
                callback(0)
            }
        }.start()
    }

    private fun secsToSmsOtpState(secs: Long): GetSmsOtpState {
        return when (secs) {
            0L -> {
                GetSmsOtpState.GetSmsOtpInitial
            }
            else -> {
                GetSmsOtpState.GetSmsOtpWaiting(secs)
            }
        }
    }

    //Flow to LiveData
    fun getSmsOTP4(countdownSecs: Long) {
        getSmsOtp4State.value = GetSmsOtpState.GetSmsOtpLoading

        viewModelScope.launch {
            repo.getSmsOtp2()
                .flowOn(Dispatchers.IO) // Works upstream, doesn't change downstream
                .flowOn(Dispatchers.Main)
                .collect { success ->
                    return@collect if (success) {
                        getSmsOtp4State.value = GetSmsOtpState.GetSmsOtpSuccess
                        getSmsOtpDelay3(countdownSecs, callback = { secs ->
                            getSmsOtp4State.value = secsToSmsOtpState(secs)
                        })
                    } else {
                        getSmsOtp4State.value = GetSmsOtpState.GetSmsOtpFail("FAIL")
                    }
                }
        }
    }

    //Flow to StateFlow
    fun getSmsOTP5(countdownSecs: Long) {
        getSmsOtp5State.value = GetSmsOtpState.GetSmsOtpLoading

        viewModelScope.launch {
            repo.getSmsOtp2()
                .map { success ->
                    if (success) {
                        getSmsOtp5State.value = GetSmsOtpState.GetSmsOtpSuccess
                        getSmsOtpDelay2(countdownSecs).collect { secs ->
                            getSmsOtp5State.value = secsToSmsOtpState(secs)
                        }
                    } else {
                        getSmsOtp5State.value = GetSmsOtpState.GetSmsOtpFail("FAIL")
                    }
                }
                .flowOn(Dispatchers.IO) // Works upstream, doesn't change downstream
                .flowOn(Dispatchers.Main)
                .collect()
        }
    }

    //RxJava to BehaviorSubject
    fun getSmsOTP6(countdownSecs: Long) {
        getSmsOtp6State.onNext(GetSmsOtpState.GetSmsOtpLoading)
        val disposable = repo.getSmsOtp()
            .doOnNext { success ->
                if (success) {
                    getSmsOtp6State.onNext(GetSmsOtpState.GetSmsOtpSuccess)
                    getSmsOtpDelay(countdownSecs)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { secs ->
                            getSmsOtp6State.onNext(secsToSmsOtpState(countdownSecs - secs))
                        }
                } else {
                    getSmsOtp6State.onNext(GetSmsOtpState.GetSmsOtpFail("FAIL"))
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        disposables.add(disposable)
    }

    fun setResp(success:Boolean) {
        if (repo is FakeAuthRepo) {
            repo.setResp(success)
        }
    }
}