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
        data class Successful(val data: List<ITunesEntity>) : HomeAction()
    }

    private val homeAction = MutableStateFlow<HomeAction>(HomeAction.Loading)
    val homeState = homeAction.asStateFlow()


    fun fetchSongs(name: String) {
        Log.i("Repository", "name: $name")
        viewModelScope.launch {
            val search = "$name".replace(" ", "+")
            getSongBySearchUseCase(search).collect { resource ->
                Log.i("Repository", "resource: $resource")
                when (resource.status) {
                    Resource.Status.LOADING -> {
                        homeAction.value = HomeAction.Loading
                    }
                    Resource.Status.SUCCESS -> {
                        if (resource.data != null) {
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