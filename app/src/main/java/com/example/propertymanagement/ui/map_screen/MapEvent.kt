package com.example.propertymanagement.ui.map_screen

sealed class MapEvent {

    data object NavigateBack : MapEvent()
    data object NavigateFilterScreen : MapEvent()

}