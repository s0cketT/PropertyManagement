package com.example.propertymanagement.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.propertymanagement.ui.bottom_nav.MainNavigation
import com.example.propertymanagement.ui.theme.PropertyManagementTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PropertyManagementTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    MainNavigation()
                }
            }
        }
    }
}

val sampleProperties = listOf(
    Property(1, 53.9168, 30.3449, PropertyType.SALE),
    Property(2, 53.9244, 30.4011, PropertyType.RENT),
    Property(3, 53.9165, 30.4372, PropertyType.SALE),
    Property(4, 53.9401, 30.3462, PropertyType.RENT),
    Property(5, 53.8960, 30.3500, PropertyType.SALE)
)




