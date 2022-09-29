package com.admaja.storyappsubmission.utils

import android.os.Looper
import android.os.Handler
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors {

    val diskIO: Executor = Executors.newSingleThreadExecutor()
    val networkIO: Executor = Executors.newFixedThreadPool(3)
    val mainThread: Executor = MainThreadExecutor()

    private class MainThreadExecutor : Executor {
        private val mainThreadExecutor = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadExecutor.post(command)
        }

    }
}