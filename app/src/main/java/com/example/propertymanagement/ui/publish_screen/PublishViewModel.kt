package com.example.propertymanagement.ui.publish_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.model.CreateProperty
import com.example.propertymanagement.domain.use_case.CreateFullPropertyUseCase
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import com.example.propertymanagement.ui.splash_screen.SplashEvent
import com.example.propertymanagement.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PublishViewModel(
    private val createFullPropertyUseCase: CreateFullPropertyUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PublishState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<PublishEvent>(viewModelScope)
    val event = _event.flow

    fun processIntent(intent: PublishIntent) {
        when (intent) {

            is PublishIntent.ClearPropertyType -> {
                val images = _state.value.imageBytes
                val title = _state.value.title

                _state.value = PublishState(
                    imageBytes = images,
                    title = title
                )
            }

            is PublishIntent.OpenCategorySelection -> {
                val images = _state.value.imageBytes
                val title = _state.value.title

                _state.value = PublishState(
                    imageBytes = images,
                    title = title
                )

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
        }
    }

    private fun upload() {
        viewModelScope.launch {

            val stateValue = _state.value

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

            runCatching {

                val request = CreateProperty(
                    ownerId = user.id,

                    type = stateValue.propertyType!!,
                    price = price,
                    currency = stateValue.currency,
                    dealType = stateValue.dealType!!,

                    title = title,

                    country = "Belarus",
                    city = "Minsk",
                    latitude = 0.0,
                    longitude = 0.0,

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
        }
    }


    private fun validateState(state: PublishState): Boolean {

        val errors = mutableListOf<String>()

        if (state.title.trim().isEmpty()) {
            _state.update { it.copy(isTitleError = true) }
            errors.add("Введите название")
        } else {
            _state.update { it.copy(isTitleError = false) }
        }

        if (state.price.toDoubleOrNull() == null) {
            _state.update { it.copy(isPriceError = true) }
            errors.add("Введите корректную цену")
        } else {
            _state.update { it.copy(isPriceError = false) }
        }

        if (state.dealType == null) {
            _state.update { it.copy(isDealTypeError = true) }
            errors.add("Выберите тип сделки")
        } else {
            _state.update { it.copy(isDealTypeError = false) }
        }

        if (state.propertyType == null) {
            _state.update { it.copy(isPropertyTypeError = true) }
            errors.add("Выберите тип недвижимости")
        } else {
            _state.update { it.copy(isPropertyTypeError = false) }
        }

        if (state.area == null) {
            _state.update { it.copy(isAreaError = true) }
            errors.add("Укажите площадь")
        } else {
            _state.update { it.copy(isAreaError = false) }
        }

        return if (errors.isNotEmpty()) {
            viewModelScope.launch {
                _event.emit(
                    PublishEvent.ShowValidationError(
                        errors.first()
                    )
                )
            }
            false
        } else {
            true
        }
    }
}