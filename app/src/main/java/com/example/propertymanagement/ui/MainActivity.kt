package com.example.propertymanagement.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.propertymanagement.data.local.dataStore
import com.example.propertymanagement.domain.model.LanguageType
import com.example.propertymanagement.ui.bottom_nav.MainNavigation
import com.example.propertymanagement.ui.settings_screen.SettingsViewModel
import com.example.propertymanagement.ui.theme.PropertyManagementTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val language = runBlocking {
            // временно — можно заменить на repository позже
            val prefs = newBase.dataStore.data.first()
            val value = prefs[stringPreferencesKey("language")] ?: LanguageType.RU.name
            LanguageType.valueOf(value)
        }

        val locale = when (language) {
            LanguageType.RU -> Locale("ru")
            LanguageType.EN -> Locale("en")
        }

        Locale.setDefault(locale)

        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)

        val context = newBase.createConfigurationContext(config)

        super.attachBaseContext(context)
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Root()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Root() {
    val viewModel: SettingsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    PropertyManagementTheme(
        themeType = state.selectedTheme
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            MainNavigation()
        }
    }
}





