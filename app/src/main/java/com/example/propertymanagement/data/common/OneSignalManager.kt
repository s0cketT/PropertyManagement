package com.example.propertymanagement.data.common

import android.content.Context
import android.util.Log
import com.example.propertymanagement.domain.model.MyAdsListingFilter
import com.onesignal.OneSignal
import com.onesignal.notifications.INotificationClickEvent
import com.onesignal.notifications.INotificationClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

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
        OneSignal.Notifications.addClickListener(
            object : INotificationClickListener {
                override fun onClick(event: INotificationClickEvent) {
                    runCatching {
                        val additionalData = event.notification.additionalData
                        handleModerationNotificationClick(additionalData)
                    }.onFailure { e ->
                        Log.e(TAG, "Failed to handle notification click", e)
                    }
                }
            },
        )
        isInitialized = true
    }

    /**
     * Cloud payload: [supabase/functions/moderate_property_ad] sends `moderation_status_id`
     * (2 = rejected, 3 = approved) and `type: property_moderation`.
     */
    private fun handleModerationNotificationClick(additionalData: JSONObject?) {
        if (additionalData == null) {
            return
        }
        val type = additionalData.optString("type", "")
        if (type != "property_moderation") {
            return
        }
        val statusId = parseModerationStatusId(additionalData) ?: return
        val filter = listingFilterFromModerationStatusId(statusId)
        PushNotificationNavigation.requestOpenMyAdsFromPush(filter)
        Log.d(TAG, "Moderation push opened: statusId=$statusId tab=$filter")
    }

    private fun parseModerationStatusId(data: JSONObject): Int? {
        if (!data.has("moderation_status_id")) {
            return null
        }
        return when (val raw = data.get("moderation_status_id")) {
            is Int -> raw
            is Long -> raw.toInt()
            is String -> raw.toIntOrNull()
            else -> null
        }
    }

    private fun listingFilterFromModerationStatusId(statusId: Int): MyAdsListingFilter =
        when (statusId) {
            2 -> MyAdsListingFilter.REJECTED
            3 -> MyAdsListingFilter.PUBLISHED
            else -> MyAdsListingFilter.PUBLISHED
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
