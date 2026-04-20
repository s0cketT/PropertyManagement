package com.example.propertymanagement.ui.edit_property_screen

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.CreateProperty
import com.example.propertymanagement.domain.model.GeocodedAddressParts
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetMyPropertiesUseCase
import com.example.propertymanagement.domain.use_case.UpdateFullPropertyUseCase
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.SingleFlowEvent
import com.example.propertymanagement.ui.edit_property_screen.components.toEditPropertyState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditPropertyViewModel(
    private val propertyId: Int,
    private val updateFullPropertyUseCase: UpdateFullPropertyUseCase,
    private val getMyPropertiesUseCase: GetMyPropertiesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    private sealed interface ValidationItem {
        data class Text(val text: String) : ValidationItem
        data class Res(@StringRes val id: Int) : ValidationItem
    }

    private val _state = MutableStateFlow(EditPropertyState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<EditPropertyEvent>(viewModelScope)
    val event = _event.flow

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingEditPayload = true) }
            val user = getCurrentUserUseCase()
            if (user == null) {
                _state.update { it.copy(isLoadingEditPayload = false) }
                _event.emit(EditPropertyEvent.NavigateBack)
                return@launch
            }
            val resource = runCatching { getMyPropertiesUseCase(user.id) }.getOrNull()
            val list: List<Property>? = when (resource) {
                is Resource.Success -> resource.data
                is Resource.Error -> null
                null -> null
            }
            val domainProperty = list?.firstOrNull { it.id == propertyId }
            if (domainProperty == null) {
                _state.update { it.copy(isLoadingEditPayload = false) }
                _event.emit(EditPropertyEvent.NavigateBack)
                return@launch
            }
            _state.update {
                domainProperty.toEditPropertyState().copy(isLoadingEditPayload = false)
            }
        }
    }

    fun processIntent(intent: EditPropertyIntent) {
        when (intent) {

            is EditPropertyIntent.ClearPropertyType -> {
                _state.update { it.copy(propertyType = null) }
            }

            is EditPropertyIntent.OpenCategorySelection -> {
                viewModelScope.launch { _event.emit(EditPropertyEvent.NavigateToCategorySelection) }
            }

            is EditPropertyIntent.SetPrice -> {
                _state.update {
                    it.copy(
                        price = intent.price,
                        isPriceError = false,
                    )
                }
            }

            is EditPropertyIntent.Submit -> {
                upload()
            }

            is EditPropertyIntent.NavigateBack -> {
                viewModelScope.launch { _event.emit(EditPropertyEvent.NavigateBack) }
            }

            is EditPropertyIntent.PickImages -> {
                viewModelScope.launch { _event.emit(EditPropertyEvent.OpenGallery) }
            }

            is EditPropertyIntent.ClearAllImages -> {
                _state.update {
                    it.copy(
                        imageBytes = emptyList(),
                        existingImageUrls = emptyList(),
                        pendingReplaceAllPhotos = true,
                    )
                }
            }

            is EditPropertyIntent.ImagesSelectedBytes -> {
                _state.update { current ->
                    current.copy(imageBytes = current.imageBytes + intent.images)
                }
            }

            is EditPropertyIntent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = intent.title,
                        isTitleError = false,
                    )
                }
            }

            is EditPropertyIntent.SetDescription -> {
                _state.update { it.copy(description = intent.text) }
            }

            is EditPropertyIntent.SetPropertyType -> {
                _state.update { it.copy(propertyType = intent.type) }
            }

            is EditPropertyIntent.SetDealType -> {
                _state.update { it.copy(dealType = intent.type) }
            }

            is EditPropertyIntent.SetCurrency -> {
                _state.update { it.copy(currency = intent.currency) }
            }

            is EditPropertyIntent.SetCommercialPropertyType -> {
                _state.update {
                    it.copy(selectedCommercialPropertyType = intent.type)
                }
            }

            is EditPropertyIntent.SetCommercialRepairType -> {
                _state.update {
                    it.copy(selectedCommercialRepairType = intent.type)
                }
            }

            is EditPropertyIntent.SetArea -> {
                _state.update { it.copy(area = intent.value) }
            }

            is EditPropertyIntent.SetSaleArea -> {
                _state.update { it.copy(saleArea = intent.value) }
            }

            is EditPropertyIntent.SetLandArea -> {
                _state.update { it.copy(landArea = intent.value) }
            }

            is EditPropertyIntent.SetFloor -> {
                _state.update { it.copy(floor = intent.value) }
            }

            is EditPropertyIntent.SetFloorHouse -> {
                _state.update { it.copy(floorHouse = intent.value) }
            }

            is EditPropertyIntent.SetCommercialAmenities -> {
                _state.update {
                    it.copy(commercialAmenities = intent.amenities)
                }
            }

            is EditPropertyIntent.SetHouseAmenities -> {
                _state.update { it.copy(houseAmenities = intent.amenities) }
            }

            is EditPropertyIntent.SetBuildingAmenities -> {
                _state.update { it.copy(buildingAmenities = intent.amenities) }
            }

            is EditPropertyIntent.SetRoomsType -> {
                _state.update { it.copy(roomsType = intent.type) }
            }

            is EditPropertyIntent.SetRoomsForSaleType -> {
                _state.update { it.copy(roomsForSaleType = intent.type) }
            }

            is EditPropertyIntent.SetWalkthroughRoom -> {
                _state.update { it.copy(isWalkthroughRoom = intent.value) }
            }

            is EditPropertyIntent.SetLivingArea -> {
                _state.update { it.copy(livingArea = intent.value) }
            }

            is EditPropertyIntent.SetKitchenArea -> {
                _state.update { it.copy(kitchenArea = intent.value) }
            }

            is EditPropertyIntent.SetBalconyType -> {
                _state.update { it.copy(balconyType = intent.type) }
            }

            is EditPropertyIntent.SetBathroomType -> {
                _state.update { it.copy(bathroomType = intent.type) }
            }

            is EditPropertyIntent.SetRepairType -> {
                _state.update { it.copy(repairType = intent.type) }
            }

            is EditPropertyIntent.SetCeilingHeight -> {
                _state.update { it.copy(ceilingHeight = intent.type) }
            }

            is EditPropertyIntent.SetWallMaterial -> {
                _state.update { it.copy(wallMaterial = intent.type) }
            }

            is EditPropertyIntent.SetYearBuilt -> {
                _state.update { it.copy(yearBuilt = intent.value) }
            }

            is EditPropertyIntent.SetRoofType -> {
                _state.update { it.copy(roofType = intent.type) }
            }
            is EditPropertyIntent.SetHeatingType -> {
                _state.update { it.copy(heatingType = intent.type) }
            }
            is EditPropertyIntent.SetWaterType -> {
                _state.update { it.copy(waterType = intent.type) }
            }
            is EditPropertyIntent.SetGasType -> {
                _state.update { it.copy(gasType = intent.type) }
            }
            is EditPropertyIntent.SetHouseType -> {
                _state.update { it.copy(houseType = intent.type) }
            }
            is EditPropertyIntent.SetParkingType -> {
                _state.update { it.copy(parkingType = intent.type) }
            }

            is EditPropertyIntent.SetAddressBottomSheetOpen -> {
                _state.update { it.copy(isAddressBottomSheetOpen = intent.open) }
            }

            is EditPropertyIntent.SetMapPickerOpen -> {
                _state.update { it.copy(isMapPickerOpen = intent.open) }
            }

            is EditPropertyIntent.SetAddressCountry -> {
                _state.update { it.copy(addressCountry = intent.value) }
            }

            is EditPropertyIntent.SetAddressRegion -> {
                _state.update { it.copy(addressRegion = intent.value) }
            }

            is EditPropertyIntent.SetAddressCity -> {
                _state.update { it.copy(addressCity = intent.value) }
            }

            is EditPropertyIntent.SetAddressStreet -> {
                _state.update { it.copy(addressStreet = intent.value) }
            }

            is EditPropertyIntent.SetAddressHouse -> {
                _state.update { it.copy(addressHouse = intent.value) }
            }

            is EditPropertyIntent.AddressSheetDone -> applyAddressSheetDone(
                latitude = intent.latitude,
                longitude = intent.longitude,
            )

            is EditPropertyIntent.ConfirmMapLocation -> confirmMapLocation(
                latitude = intent.latitude,
                longitude = intent.longitude,
                geocoded = intent.geocoded,
            )
        }
    }

    private fun applyAddressSheetDone(latitude: Double?, longitude: Double?) {
        _state.update {
            it.copy(
                isAddressBottomSheetOpen = false,
                latitude = latitude ?: it.latitude,
                longitude = longitude ?: it.longitude,
                isLocationError = if (latitude != null && longitude != null) false else it.isLocationError,
            )
        }
    }

    private fun confirmMapLocation(
        latitude: Double,
        longitude: Double,
        geocoded: GeocodedAddressParts,
    ) {
        _state.update {
            it.copy(
                latitude = latitude,
                longitude = longitude,
                addressCountry = geocoded.country.ifBlank { it.addressCountry },
                addressRegion = geocoded.region.ifBlank { it.addressRegion },
                addressCity = geocoded.city.ifBlank { it.addressCity },
                addressStreet = geocoded.street.ifBlank { it.addressStreet },
                addressHouse = geocoded.house.ifBlank { it.addressHouse },
                isMapPickerOpen = false,
                isLocationError = false,
            )
        }
    }

    private fun upload() {
        viewModelScope.launch {

            val stateValue = _state.value

            if (stateValue.isPublishing) return@launch

            val user = getCurrentUserUseCase()

            if (user == null) {
                _event.emit(EditPropertyEvent.ShowAuthRequired)
                return@launch
            }

            if (!validateState(stateValue)) {
                return@launch
            }

            val title = stateValue.title.trim()
            val price = stateValue.price.toDoubleOrNull()!!

            _state.update { it.copy(isPublishing = true) }
            try {
                runCatching {
                    val request = CreateProperty(
                        ownerId = user.id,

                        type = stateValue.propertyType!!,
                        price = price,
                        currency = stateValue.currency,
                        dealType = stateValue.dealType!!,

                        title = title,

                        description = stateValue.description.trim().takeIf { it.isNotEmpty() },

                        country = stateValue.addressCountry.ifBlank { DEFAULT_COUNTRY },
                        region = stateValue.addressRegion.trim(),
                        city = stateValue.addressCity.trim(),
                        street = stateValue.addressStreet.trim(),
                        house = stateValue.addressHouse.trim(),
                        latitude = stateValue.latitude!!,
                        longitude = stateValue.longitude!!,

                        area = stateValue.area!!.toDouble(),

                        rooms = stateValue.roomsType,
                        floor = stateValue.floor,
                        totalFloors = stateValue.floorHouse,
                        yearBuilt = stateValue.yearBuilt,

                        commercialAmenities = stateValue.commercialAmenities,
                        buildingAmenities = stateValue.buildingAmenities,
                        houseAmenities = stateValue.houseAmenities,

                        commercialType = stateValue.selectedCommercialPropertyType,
                        commercialRepairType = stateValue.selectedCommercialRepairType,

                        isWalkthrough = stateValue.isWalkthroughRoom,
                        livingArea = stateValue.livingArea?.toDouble(),
                        kitchenArea = stateValue.kitchenArea?.toDouble(),
                        bathroomType = stateValue.bathroomType,
                        balconyType = stateValue.balconyType,
                        ceilingHeight = stateValue.ceilingHeight,
                        repairType = stateValue.repairType,
                        wallMaterial = stateValue.wallMaterial,

                        roomsForSale = stateValue.roomsForSaleType,
                        saleArea = stateValue.saleArea?.toDouble(),

                        houseType = stateValue.houseType,
                        landArea = stateValue.landArea?.toDouble(),
                        floors = stateValue.floorHouse,
                        roofType = stateValue.roofType,
                        heatingType = stateValue.heatingType,
                        waterType = stateValue.waterType,
                        gasType = stateValue.gasType,

                        parkingType = stateValue.parkingType,
                    )

                    val removeRemoteImages = stateValue.pendingReplaceAllPhotos ||
                        (
                            stateValue.imageBytes.isEmpty() &&
                                stateValue.existingImageUrls.isEmpty() &&
                                stateValue.hadRemotePhotosWhenLoaded
                            )

                    updateFullPropertyUseCase(
                        propertyId = propertyId,
                        request = request,
                        newImageBytes = stateValue.imageBytes,
                        removeExistingImagesFirst = removeRemoteImages,
                    )
                }
                    .onSuccess {
                        _event.emit(EditPropertyEvent.SaveSuccess)
                    }
                    .onFailure { e ->
                        Log.e("EDIT_PROPERTY", "Update failed: ${e.message}", e)
                    }
            } finally {
                _state.update { it.copy(isPublishing = false) }
            }
        }
    }

    private companion object {

        private const val DEFAULT_COUNTRY = "Belarus"
    }

    private fun validateState(state: EditPropertyState): Boolean {

        val errors = mutableListOf<ValidationItem>()

        if (state.title.trim().isEmpty()) {
            _state.update { it.copy(isTitleError = true) }
            errors.add(ValidationItem.Text("Введите название"))
        } else {
            _state.update { it.copy(isTitleError = false) }
        }

        if (state.price.toDoubleOrNull() == null) {
            _state.update { it.copy(isPriceError = true) }
            errors.add(ValidationItem.Text("Введите корректную цену"))
        } else {
            _state.update { it.copy(isPriceError = false) }
        }

        if (state.dealType == null) {
            _state.update { it.copy(isDealTypeError = true) }
            errors.add(ValidationItem.Text("Выберите тип сделки"))
        } else {
            _state.update { it.copy(isDealTypeError = false) }
        }

        if (state.propertyType == null) {
            _state.update { it.copy(isPropertyTypeError = true) }
            errors.add(ValidationItem.Text("Выберите тип недвижимости"))
        } else {
            _state.update { it.copy(isPropertyTypeError = false) }
        }

        if (state.area == null) {
            _state.update { it.copy(isAreaError = true) }
            errors.add(ValidationItem.Text("Укажите площадь"))
        } else {
            _state.update { it.copy(isAreaError = false) }
        }

        if (state.latitude == null || state.longitude == null) {
            _state.update { it.copy(isLocationError = true) }
            errors.add(ValidationItem.Res(R.string.publish_validation_location))
        } else {
            _state.update { it.copy(isLocationError = false) }
        }

        return if (errors.isNotEmpty()) {
            viewModelScope.launch {
                when (val first = errors.first()) {
                    is ValidationItem.Text ->
                        _event.emit(EditPropertyEvent.ShowValidationError(first.text))
                    is ValidationItem.Res ->
                        _event.emit(EditPropertyEvent.ShowValidationErrorRes(first.id))
                }
            }
            false
        } else {
            true
        }
    }
}
