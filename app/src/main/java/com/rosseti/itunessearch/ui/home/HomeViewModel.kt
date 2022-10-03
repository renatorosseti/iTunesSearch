package com.rosseti.itunessearch.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosseti.domain.Resource
import com.rosseti.domain.entity.ITunesEntity
import com.rosseti.domain.usecase.GetSongBySearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSongBySearchUseCase: GetSongBySearchUseCase,
) :
    ViewModel() {

    sealed class HomeAction {
        object Loading : HomeAction()
        object Error : HomeAction()
        object Empty : HomeAction()
        data class Successful(val data: List<ITunesEntity>) : HomeAction()
    }

    private val homeAction = MutableStateFlow<HomeAction>(HomeAction.Empty)
    val homeState = homeAction.asStateFlow()


    fun fetchSongs(search: String) {
        viewModelScope.launch {
            getSongBySearchUseCase(search).collect { resource ->
                Log.i("Repository", "Data: ${resource.data}")
                when (resource.status) {
                    Resource.Status.LOADING -> {
                        homeAction.value = HomeAction.Loading
                    }
                    Resource.Status.SUCCESS -> {
                        if (resource.data.isNullOrEmpty()) {
                            homeAction.value = HomeAction.Empty
                        } else {
                            homeAction.value = HomeAction.Successful(resource.data ?: listOf())
                        }
                    }
                    Resource.Status.ERROR -> {
                        homeAction.value = HomeAction.Error
                    }
                }
            }
        }
    }
}