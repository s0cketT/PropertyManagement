package com.example.propertymanagement.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.propertymanagement.domain.model.ApplicationStatus

data class ApplicationStatusPalette(
    val badgeContainer: Color,
    val badgeContent: Color,
    val cardContainer: Color,
    val accent: Color,
)

object ApplicationStatusColors {

    fun palette(status: ApplicationStatus, isDarkTheme: Boolean): ApplicationStatusPalette {
        return when (status) {
            ApplicationStatus.PROCESSING -> if (isDarkTheme) {
                ApplicationStatusPalette(
                    badgeContainer = Color(0xFF4527A0),
                    badgeContent = Color(0xFFEDE7F6),
                    cardContainer = Color(0xFF252033),
                    accent = Color(0xFF9F8FEF),
                )
            } else {
                ApplicationStatusPalette(
                    badgeContainer = Color(0xFF673AB7),
                    badgeContent = Color(0xFFFFFFFF),
                    cardContainer = Color(0xFFF7F5FC),
                    accent = Color(0xFF7C6FD6),
                )
            }

            ApplicationStatus.IN_PROGRESS -> if (isDarkTheme) {
                ApplicationStatusPalette(
                    badgeContainer = Color(0xFF0D47A1),
                    badgeContent = Color(0xFFE3F2FD),
                    cardContainer = Color(0xFF152535),
                    accent = Color(0xFF42A5F5),
                )
            } else {
                ApplicationStatusPalette(
                    badgeContainer = Color(0xFF1565C0),
                    badgeContent = Color(0xFFFFFFFF),
                    cardContainer = Color(0xFFEEF6FD),
                    accent = Color(0xFF1E88E5),
                )
            }

            ApplicationStatus.REJECTED -> if (isDarkTheme) {
                ApplicationStatusPalette(
                    badgeContainer = Color(0xFFB71C1C),
                    badgeContent = Color(0xFFFFEBEE),
                    cardContainer = Color(0xFF2A1818),
                    accent = Color(0xFFEF5350),
                )
            } else {
                ApplicationStatusPalette(
                    badgeContainer = Color(0xFFC62828),
                    badgeContent = Color(0xFFFFFFFF),
                    cardContainer = Color(0xFFFFF5F5),
                    accent = Color(0xFFE53935),
                )
            }

            ApplicationStatus.COMPLETED -> if (isDarkTheme) {
                ApplicationStatusPalette(
                    badgeContainer = Color(0xFF1B5E20),
                    badgeContent = Color(0xFFE8F5E9),
                    cardContainer = Color(0xFF182A1E),
                    accent = Color(0xFF66BB6A),
                )
            } else {
                ApplicationStatusPalette(
                    badgeContainer = Color(0xFF2E7D32),
                    badgeContent = Color(0xFFFFFFFF),
                    cardContainer = Color(0xFFF2FAF3),
                    accent = Color(0xFF43A047),
                )
            }

            ApplicationStatus.UNKNOWN -> if (isDarkTheme) {
                ApplicationStatusPalette(
                    badgeContainer = Color(0xFF37474F),
                    badgeContent = Color(0xFFECEFF1),
                    cardContainer = Color(0xFF1A2224),
                    accent = Color(0xFF90A4AE),
                )
            } else {
                ApplicationStatusPalette(
                    badgeContainer = Color(0xFF546E7A),
                    badgeContent = Color(0xFFFFFFFF),
                    cardContainer = Color(0xFFF5F5F5),
                    accent = Color(0xFF78909C),
                )
            }
        }
    }
}
