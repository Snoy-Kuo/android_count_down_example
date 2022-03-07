package com.snoy.count_down_example.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// ref = https://stackoverflow.com/a/70019091
fun tickerFlow(start: Long, end: Long): Flow<Long> {
    return flow {
        for (i in start downTo end) {
            emit(i)
            delay(1000L)
        }
    }
}
