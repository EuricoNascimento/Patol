package com.eurico.patol.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eurico.patol.model.ConsultResult
import com.eurico.patol.model.database.MaterialDTO
import com.eurico.patol.repository.MaterialRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.eurico.patol.R

class MaterialListViewModel(
    private val materialRepository: MaterialRepositoryImpl
): ViewModel() {
    private val _enableLoading = MutableStateFlow(mutableStateOf(true))
    val enableLoading: StateFlow<MutableState<Boolean>> get() = _enableLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow(mutableIntStateOf(0))
    val errorMessage: StateFlow<MutableState<Int>> get() = _errorMessage.asStateFlow()

    private val _materialList = MutableStateFlow(mutableStateOf(listOf<MaterialDTO>()))
    val materialList: StateFlow<MutableState<List<MaterialDTO>>> get() = _materialList.asStateFlow()

    fun initialize() {
        viewModelScope.launch(Dispatchers.IO) {
            materialRepository.getAllMaterialsFromDataBase().collect {
                when(it) {
                    is ConsultResult.Loading -> _enableLoading.value.value = true
                    is ConsultResult.Error -> {
                        _enableLoading.value.value = false
                        _errorMessage.value.intValue = R.string.error_phrases
                    }
                    is ConsultResult.Success -> {
                        _enableLoading.value.value = false
                        _materialList.value.value = it.data
                    }
                }
            }
        }
    }
}