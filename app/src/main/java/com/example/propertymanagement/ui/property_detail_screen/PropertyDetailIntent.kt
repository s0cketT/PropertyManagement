package com.example.propertymanagement.ui.property_detail_screen

import com.example.propertymanagement.domain.model.NearbyPoiCategory

sealed class PropertyDetailIntent {

    data object NavigateBack : PropertyDetailIntent()

    data class SetMapFullscreen(val open: Boolean) : PropertyDetailIntent()

    data class SetMapPoiCategoryVisible(
        val category: NearbyPoiCategory,
        val visible: Boolean,
    ) : PropertyDetailIntent()

    data class OpenImageViewer(val pageIndex: Int) : PropertyDetailIntent()

    data object CloseImageViewer : PropertyDetailIntent()

    data object ToggleFavorite : PropertyDetailIntent()

    data object SubmitRequest : PropertyDetailIntent()

    data object DismissApplicationSheet : PropertyDetailIntent()

    data class SetApplicationComment(val text: String) : PropertyDetailIntent()

    data object ConfirmApplicationSubmit : PropertyDetailIntent()
}
