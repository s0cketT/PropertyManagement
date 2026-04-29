package com.example.propertymanagement.ui.splash_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.data.common.OneSignalManager
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _event = SingleFlowEvent<SplashEvent>(viewModelScope)
    val event = _event.flow

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            runCatching {
                delay(1500)
                getCurrentUserUseCase()
            }.onSuccess { user ->
                Log.d("!!!", user.toString())
                if (user != null) {
                    OneSignalManager.loginAfterPermission(user.id)
                    _event.emit(SplashEvent.NavigateToMain)
                } else {
                    OneSignalManager.logout()
                    _event.emit(SplashEvent.NavigateToAuth)
                }
            }.onFailure {
                Log.d("!!!", it.toString())
                OneSignalManager.logout()
                _event.emit(SplashEvent.NavigateToAuth)
            }
        }
    }
}