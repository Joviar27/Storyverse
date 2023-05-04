package com.example.storyverse.ui.addstory

import androidx.lifecycle.ViewModel
import com.example.storyverse.domain.usecase.AddStoryUseCase
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val addStoryUseCase: AddStoryUseCase) : ViewModel(){

    fun addStoryWithoutLocation(imageMultipart : MultipartBody.Part, description : RequestBody)= addStoryUseCase.addStoryWithoutLocation(imageMultipart, description)
    fun addStoryWithLocation(imageMultipart : MultipartBody.Part, description : RequestBody, lat : RequestBody, lon : RequestBody)=
        addStoryUseCase.addStoryWithLocation(imageMultipart, description, lat, lon)
}