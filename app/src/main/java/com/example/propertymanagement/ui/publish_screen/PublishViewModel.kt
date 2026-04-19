package com.example.propertymanagement.ui.publish_screen

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.model.CreateProperty
import com.example.propertymanagement.domain.model.GeocodedAddressParts
import com.example.propertymanagement.domain.use_case.CreateFullPropertyUseCase
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.SingleFlowEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PublishViewModel(
    private val createFullPropertyUseCase: CreateFullPropertyUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private sealed interface ValidationItem {
        data class Text(val text: String) : ValidationItem
        data class Res(@StringRes val id: Int) : ValidationItem
    }

    private val _state = MutableStateFlow(PublishState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<PublishEvent>(viewModelScope)
    val event = _event.flow

    fun processIntent(intent: PublishIntent) {
        when (intent) {

            is PublishIntent.ClearPropertyType -> {
                _state.update { preserveImagesTitleAndLocation(it).copy(propertyType = null) }
            }

            is PublishIntent.OpenCategorySelection -> {
                _state.update { preserveImagesTitleAndLocation(it) }

                viewModelScope.launch { _event.emit(PublishEvent.NavigateToCategorySelection) }
            }

            is PublishIntent.SetPrice -> {
                _state.update {
                    it.copy(
                        price = intent.price,
                        isPriceError = false
                    )
                }
            }

            is PublishIntent.Submit -> { upload() }

            is PublishIntent.NavigateBack -> {
                viewModelScope.launch { _event.emit(PublishEvent.NavigateBack) }
            }

            is PublishIntent.PickImages -> {
                viewModelScope.launch { _event.emit(PublishEvent.OpenGallery) }
            }

            is PublishIntent.ImagesSelectedBytes -> {
                _state.update { it.copy(imageBytes = intent.images) }
            }

            is PublishIntent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = intent.title,
                        isTitleError = false
                    )
                }
            }

            is PublishIntent.SetDescription -> {
                _state.update { it.copy(description = intent.text) }
            }

            is PublishIntent.SetPropertyType -> {
                _state.update { it.copy(propertyType = intent.type) }
            }

            is PublishIntent.SetDealType -> {
                _state.update { it.copy(dealType = intent.type) }
            }

            is PublishIntent.SetCurrency -> {
                _state.update { it.copy(currency = intent.currency) }
            }

            is PublishIntent.SetCommercialPropertyType -> {
                _state.update {
                    it.copy(selectedCommercialPropertyType = intent.type)
                }
            }

            is PublishIntent.SetCommercialRepairType -> {
                _state.update {
                    it.copy(selectedCommercialRepairType = intent.type)
                }
            }

            is PublishIntent.SetArea -> {
                _state.update { it.copy(area = intent.value) }
            }

            is PublishIntent.SetSaleArea -> {
                _state.update { it.copy(saleArea = intent.value) }
            }

            is PublishIntent.SetLandArea -> {
                _state.update { it.copy(landArea = intent.value) }
            }

            is PublishIntent.SetFloor -> {
                _state.update { it.copy(floor = intent.value) }
            }

            is PublishIntent.SetFloorHouse -> {
                _state.update { it.copy(floorHouse = intent.value) }
            }

            is PublishIntent.SetCommercialAmenities -> {
                _state.update {
                    it.copy(commercialAmenities = intent.amenities)
                }
            }

            is PublishIntent.SetHouseAmenities -> {
                _state.update { it.copy(houseAmenities = intent.amenities) }
            }

            is PublishIntent.SetBuildingAmenities -> {
                _state.update { it.copy(buildingAmenities = intent.amenities) }
            }

            is PublishIntent.SetRoomsType -> {
                _state.update { it.copy(roomsType = intent.type) }
            }

            is PublishIntent.SetRoomsForSaleType -> {
                _state.update { it.copy(roomsForSaleType = intent.type) }
            }

            is PublishIntent.SetWalkthroughRoom -> {
                _state.update { it.copy(isWalkthroughRoom = intent.value) }
            }

            is PublishIntent.SetLivingArea -> {
                _state.update { it.copy(livingArea = intent.value) }
            }

            is PublishIntent.SetKitchenArea -> {
                _state.update { it.copy(kitchenArea = intent.value) }
            }

            is PublishIntent.SetBalconyType -> {
                _state.update { it.copy(balconyType = intent.type) }
            }

            is PublishIntent.SetBathroomType -> {
                _state.update { it.copy(bathroomType = intent.type) }
            }

            is PublishIntent.SetRepairType -> {
                _state.update { it.copy(repairType = intent.type) }
            }

            is PublishIntent.SetCeilingHeight -> {
                _state.update { it.copy(ceilingHeight = intent.type) }
            }

            is PublishIntent.SetWallMaterial -> {
                _state.update { it.copy(wallMaterial = intent.type) }
            }

            is PublishIntent.SetYearBuilt -> {
                _state.update { it.copy(yearBuilt = intent.value) }
            }

            is PublishIntent.SetRoofType -> {
                _state.update { it.copy(roofType = intent.type) }
            }
            is PublishIntent.SetHeatingType -> {
                _state.update { it.copy(heatingType = intent.type) }
            }
            is PublishIntent.SetWaterType -> {
                _state.update { it.copy(waterType = intent.type) }
            }
            is PublishIntent.SetGasType -> {
                _state.update { it.copy(gasType = intent.type) }
            }
            is PublishIntent.SetHouseType -> {
                _state.update { it.copy(houseType = intent.type) }
            }
            is PublishIntent.SetParkingType -> {
                _state.update { it.copy(parkingType = intent.type) }
            }

            is PublishIntent.SetAddressBottomSheetOpen -> {
                _state.update { it.copy(isAddressBottomSheetOpen = intent.open) }
            }

            is PublishIntent.SetMapPickerOpen -> {
                _state.update { it.copy(isMapPickerOpen = intent.open) }
            }

            is PublishIntent.SetAddressCountry -> {
                _state.update { it.copy(addressCountry = intent.value) }
            }

            is PublishIntent.SetAddressRegion -> {
                _state.update { it.copy(addressRegion = intent.value) }
            }

            is PublishIntent.SetAddressCity -> {
                _state.update { it.copy(addressCity = intent.value) }
            }

            is PublishIntent.SetAddressStreet -> {
                _state.update { it.copy(addressStreet = intent.value) }
            }

            is PublishIntent.SetAddressHouse -> {
                _state.update { it.copy(addressHouse = intent.value) }
            }

            is PublishIntent.AddressSheetDone -> applyAddressSheetDone(
                latitude = intent.latitude,
                longitude = intent.longitude
            )

            is PublishIntent.ConfirmMapLocation -> confirmMapLocation(
                latitude = intent.latitude,
                longitude = intent.longitude,
                geocoded = intent.geocoded
            )
        }
    }

    private fun preserveImagesTitleAndLocation(current: PublishState): PublishState {
        return PublishState(
            imageBytes = current.imageBytes,
            title = current.title,
            description = current.description,
            addressCountry = current.addressCountry,
            addressRegion = current.addressRegion,
            addressCity = current.addressCity,
            addressStreet = current.addressStreet,
            addressHouse = current.addressHouse,
            latitude = current.latitude,
            longitude = current.longitude,
            isPublishing = current.isPublishing
        )
    }

    private fun applyAddressSheetDone(latitude: Double?, longitude: Double?) {
        _state.update {
            it.copy(
                isAddressBottomSheetOpen = false,
                latitude = latitude ?: it.latitude,
                longitude = longitude ?: it.longitude,
                isLocationError = if (latitude != null && longitude != null) false else it.isLocationError
            )
        }
    }

    private fun confirmMapLocation(
        latitude: Double,
        longitude: Double,
        geocoded: GeocodedAddressParts
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
                isLocationError = false
            )
        }
    }

    private fun upload() {
        viewModelScope.launch {

            val stateValue = _state.value

            if (stateValue.isPublishing) return@launch

            val user = getCurrentUserUseCase()

            if (user == null) {
                _event.emit(PublishEvent.ShowAuthRequired)
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

                    parkingType = stateValue.parkingType
                )

                    createFullPropertyUseCase(
                        request = request,
                        imageBytes = stateValue.imageBytes
                    )
                }
                    .onSuccess {
                        _event.emit(PublishEvent.NavigateBack)
                    }
                    .onFailure { e ->
                        Log.e("UPLOAD", "Upload failed: ${e.message}", e)
                    }
            } finally {
                _state.update { it.copy(isPublishing = false) }
            }
        }
    }

    private companion object {

        private const val DEFAULT_COUNTRY = "Belarus"
    }

    private fun validateState(state: PublishState): Boolean {

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
                        _event.emit(PublishEvent.ShowValidationError(first.text))
                    is ValidationItem.Res ->
                        _event.emit(PublishEvent.ShowValidationErrorRes(first.id))
                }
            }
            false
        } else {
            true
        }
    }
}