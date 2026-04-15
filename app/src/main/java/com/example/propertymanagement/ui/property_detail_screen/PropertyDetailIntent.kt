package com.example.propertymanagement.ui.property_detail_screen

sealed class PropertyDetailIntent {

    data object NavigateBack : PropertyDetailIntent()

    data class SetMapFullscreen(val open: Boolean) : PropertyDetailIntent()

    data class OpenImageViewer(val pageIndex: Int) : PropertyDetailIntent()

    data object CloseImageViewer : PropertyDetailIntent()

    data object ToggleFavorite : PropertyDetailIntent()

    data object SubmitRequest : PropertyDetailIntent()
}
