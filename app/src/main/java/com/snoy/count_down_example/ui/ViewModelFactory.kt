package com.snoy.count_down_example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.snoy.count_down_example.model.repo.AuthRepo
import com.snoy.count_down_example.model.repo.Repository
import com.snoy.count_down_example.ui.main.MainViewModel

class ViewModelFactory(private val repo: Repository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return with(modelClass) {

            when {
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repo as AuthRepo)

                else ->
                    throw IllegalArgumentException(
                        "Unknown ViewModel class: ${modelClass.name}"
                    )
            }

        } as T
    }
}