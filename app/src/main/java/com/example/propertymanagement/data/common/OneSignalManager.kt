package com.example.propertymanagement.data.common

import android.content.Context
import android.util.Log
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object OneSignalManager {

    private const val TAG = "OneSignalManager"
    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) {
            return
        }

        val appId = Constants.ONESIGNAL_APP_ID.trim()
        if (appId.isEmpty()) {
            Log.w(TAG, "OneSignal init skipped: ONESIGNAL_APP_ID is empty.")
            return
        }

        OneSignal.initWithContext(context.applicationContext, appId)
        isInitialized = true
    }

    fun requestPermission(fallbackToSettings: Boolean = true) {
        if (!isInitialized) {
            return
        }
        CoroutineScope(Dispatchers.Main.immediate).launch {
            OneSignal.Notifications.requestPermission(fallbackToSettings)
        }
    }

    fun login(externalUserId: String?) {
        if (!isInitialized || externalUserId.isNullOrBlank()) {
            return
        }
        OneSignal.login(externalUserId)
    }

    fun loginAfterPermission(externalUserId: String?, fallbackToSettings: Boolean = true) {
        if (!isInitialized || externalUserId.isNullOrBlank()) {
            return
        }

        CoroutineScope(Dispatchers.Main.immediate).launch {
            OneSignal.Notifications.requestPermission(fallbackToSettings)
            delay(1200)

            val isPermissionGranted = OneSignal.Notifications.permission
            if (!isPermissionGranted) {
                Log.w(TAG, "OneSignal login skipped: notification permission is not granted yet.")
                return@launch
            }

            var pushToken: String? = null
            for (attempt in 0 until 6) {
                val currentToken = OneSignal.User.pushSubscription.token
                if (!currentToken.isNullOrBlank()) {
                    pushToken = currentToken
                    break
                }
                delay(500)
            }

            if (pushToken.isNullOrBlank()) {
                Log.w(TAG, "OneSignal push token is still empty before login.")
            }

            OneSignal.login(externalUserId)
            Log.d(
                TAG,
                "OneSignal login completed. permission=$isPermissionGranted tokenPresent=${!pushToken.isNullOrBlank()}",
            )
        }
    }

    fun logout() {
        if (!isInitialized) {
            return
        }
        OneSignal.logout()
    }
}
