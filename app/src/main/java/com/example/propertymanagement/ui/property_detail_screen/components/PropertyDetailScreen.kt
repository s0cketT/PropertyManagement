package com.example.propertymanagement.ui.property_detail_screen.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.propertymanagement.domain.model.Property

@Composable
fun PropertyDetailScreen(
    navController: NavController,
    propertyId: Int,
    userId: String
) {
    Log.d("!!!", "property - $propertyId, userId = $userId")
}