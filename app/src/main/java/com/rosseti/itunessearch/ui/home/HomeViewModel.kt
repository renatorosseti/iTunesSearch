package com.rosseti.itunessearch.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosseti.domain.Resource
import com.rosseti.domain.entity.ITunesEntity
import com.rosseti.domain.usecase.GetSongByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSongByNameUseCase: GetSongByNameUseCase,
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
        viewModelScope.launch {
            getSongByNameUseCase(name.replace(" ", "+")).collect { resource ->
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