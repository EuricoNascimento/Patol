package com.eurico.patol.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eurico.patol.R
import com.eurico.patol.model.ConsultResult
import com.eurico.patol.repository.MaterialRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoadingViewModel(
    private val materialRepository: MaterialRepositoryImpl
): ViewModel() {
    private val _loginPhrase = MutableStateFlow(mutableIntStateOf(0))
    val loginPhrase: StateFlow<MutableState<Int>> get() = _loginPhrase.asStateFlow()

    fun initialize() {
         viewModelScope.launch(Dispatchers.IO) {
             materialRepository.checkMaterial().collect{
                 when (it) {
                     is ConsultResult.Error ->
                         _loginPhrase.value.intValue = it.exception.message?.toIntOrNull() ?: R.string.download_error
                     is ConsultResult.Loading -> _loginPhrase.value.intValue = 0
                     is ConsultResult.Success -> _loginPhrase.value.intValue = R.string.sucess_login

                 }
             }
         }
    }
}