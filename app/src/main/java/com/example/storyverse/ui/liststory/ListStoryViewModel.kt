package com.example.storyverse.ui.liststory

import androidx.lifecycle.ViewModel
import com.example.storyverse.domain.usecase.GetStoryListUseCase
import com.example.storyverse.domain.usecase.LogoutUseCase

class ListStoryViewModel(
    private val getStoryListUseCase: GetStoryListUseCase,
    private val logoutUseCase: LogoutUseCase
    ) : ViewModel() {
    fun getStoryList(location : Int) = getStoryListUseCase.execute(location)
    fun logout() = logoutUseCase.execute()
}