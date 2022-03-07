# count_down_example

An Android example App that simulates "get SMS OTP" flow:
 - click the "get SMS OTP" button
 - disable the button and show progressbar while waiting for response
 - count down form 10 to 0
 - the button can be clicked again

## Dev env

 - macOS 11.6 (Big Sur) x64
 - Android Studio Bumblebee Patch 1
 - Android SDK version 31
 - JDK: 11
 - Gradle: 7.2
 - Kotlin: 1.6.10
 - RxJava: 3.1.3

 ## References

 - [RxJava2系列实践之倒计时功能(三)](https://www.jianshu.com/p/44c6503c2f11)
 - [Day 7 ViewModel (Last) 應用與心得總結](https://ithelp.ithome.com.tw/articles/10218512)
 - [View Binding](https://developer.android.com/topic/libraries/view-binding)
 - [How to create a countdown with flow coroutines](https://stackoverflow.com/a/70019091)
 - [LiveData Overview](https://developer.android.com/topic/libraries/architecture/livedata)
 - [CountDownTimer](https://developer.android.com/reference/android/os/CountDownTimer)
 - [RxJava2 and LiveData](https://ithelp.ithome.com.tw/articles/10197420)
 - [Sealed interfaces in Kotlin](https://jorgecastillo.dev/sealed-interfaces-kotlin)
 - [Kotlin flows on Android](https://developer.android.com/kotlin/flow)
 - [StateFlow and SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)

 ## Libraries

 - [rxjava](https://github.com/ReactiveX/RxJava)
 - [rxandroid](https://github.com/ReactiveX/RxAndroid)
 - [kotlinx-coroutines-android](https://developer.android.com/kotlin/coroutines)
 - [lifecycle-runtime-ktx](https://developer.android.com/jetpack/androidx/releases/lifecycle)
 - [lifecycle-viewmodel-ktx](https://developer.android.com/jetpack/androidx/releases/lifecycle)


 ## Todos

 - update UI by RxJava BehaviorSubject.