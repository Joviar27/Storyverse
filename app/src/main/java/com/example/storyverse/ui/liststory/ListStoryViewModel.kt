package com.example.storyverse.ui.liststory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyverse.domain.entity.StoryEntity
import com.example.storyverse.domain.usecase.GetStoryListUseCase
import com.example.storyverse.domain.usecase.LogoutUseCase

class ListStoryViewModel(
    private val getStoryListUseCase: GetStoryListUseCase,
    private val logoutUseCase: LogoutUseCase
    ) : ViewModel() {
    fun getStoryList(location : Int) = getStoryListUseCase.getStoryList(location)

    val story : LiveData<PagingData<StoryEntity>> =
        getStoryListUseCase.getStoryPaged().cachedIn(viewModelScope)

    fun logout() = logoutUseCase.execute()
}