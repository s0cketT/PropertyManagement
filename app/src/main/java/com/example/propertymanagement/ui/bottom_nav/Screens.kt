package com.example.propertymanagement.ui.bottom_nav

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.UserProfile
import com.example.propertymanagement.ui.auth_screen.AuthCheck
import com.example.propertymanagement.ui.extensions.toJson

sealed class Screens(
    val route: String,
    val titleResId: Int? = null,
    val icon: ImageVector? = null
) {
    @Composable
    fun title(): String = stringResource(titleResId!!)

    object Advertisements : Screens(
        route = "advertisements",
        titleResId = R.string.screen_advertisements,
        icon = Icons.Default.Home
    )

    object Favorites : Screens(
        route = "favorites",
        titleResId = R.string.screen_favorites,
        icon = Icons.Default.Favorite
    )

    object Map : Screens(
        route = "map",
        titleResId = R.string.screen_map,
        icon = Icons.Default.Place
    )

    object Publish : Screens(
        route = "publish",
        titleResId = R.string.screen_publish,
        icon = Icons.Default.AddCircle
    )

    object EditPropertyScreen : Screens("edit_property/{propertyId}") {

        fun createRoute(propertyId: Int): String = "edit_property/$propertyId"
    }

    object Profile : Screens(
        route = "profile",
        titleResId = R.string.screen_profile,
        icon = Icons.Default.Person
    )

    object Filters : Screens("filters")

    object CategorySelection : Screens("category_selection")

    object AuthRegister : Screens("auth_register")

    object AuthOtpScreen : Screens("auth_otp?email={email}&check={check}") {

        fun createRoute(email: String, check: AuthCheck): String {
            val encoded = Uri.encode(email)
            return "auth_otp?email=$encoded&check=${check.name}"
        }
    }

    object SetNewPasswordScreen : Screens("set_new_password")

    object ChangeNewEmailScreen : Screens("change_email_new")

    object ChangeEmailLinkScreen : Screens("change_email_link?email={email}") {
        fun createRoute(email: String): String {
            val encoded = Uri.encode(email)
            return "change_email_link?email=$encoded"
        }
    }

    object AuthLoginScreen : Screens("auth_login")

    object SplashScreen : Screens("splash")

    object SettingsScreen : Screens("settings")
    object MyAdsScreen : Screens("my_ads")
    object PersonalInfoScreen : Screens("personal_info?user={user}") {

        fun createRoute(user: UserProfile): String {
            val json = Uri.encode(user.toJson())
            return "personal_info?user=$json"
        }
    }

    object PropertyDetailScreen : Screens("property_detail?propertyId={propertyId}&userId={userId}") {

        fun createRoute(propertyId: Int, userId: String): String {
            return "property_detail?propertyId=$propertyId&userId=$userId"
        }
    }
}


