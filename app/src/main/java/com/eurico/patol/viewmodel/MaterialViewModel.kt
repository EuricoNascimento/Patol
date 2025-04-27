package com.eurico.patol.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eurico.patol.model.ConsultResult
import com.eurico.patol.model.database.ContentDTO
import com.eurico.patol.repository.MaterialRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MaterialViewModel(
    private val materialRepositoryImpl: MaterialRepositoryImpl
): ViewModel() {
    private val _enableLoading = MutableStateFlow(mutableStateOf(true))
    val enableLoading: StateFlow<MutableState<Boolean>> get() = _enableLoading.asStateFlow()
    private val _contentList = MutableStateFlow(mutableStateOf(listOf<ContentDTO>()))
    val contentList: StateFlow<MutableState<List<ContentDTO>>> get() = _contentList.asStateFlow()
    private val _error = MutableStateFlow(mutableStateOf(false))
    val error: StateFlow<MutableState<Boolean>> get() = _error.asStateFlow()

    fun initialize(materialId: Long?) {
        if (materialId == null) {
            _error.value.value = true
            _enableLoading.value.value = false
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            materialRepositoryImpl.getAllContentByMaterialId(materialId).collect {
                when(it) {
                    is ConsultResult.Loading -> {
                        _error.value.value = false
                        _enableLoading.value.value = true
                    }
                    is ConsultResult.Error -> {
                        _error.value.value = true
                        _enableLoading.value.value = false
                    }
                    is ConsultResult.Success -> {
                        _contentList.value.value = it.data
                        _error.value.value = false
                        _enableLoading.value.value = false
                    }
                }
            }
        }
    }
}