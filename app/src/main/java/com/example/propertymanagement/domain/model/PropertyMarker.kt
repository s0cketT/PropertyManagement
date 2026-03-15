package com.example.propertymanagement.domain.model

enum class PropertyType {
    COMMERCIAL,
    APARTMENT,
    ROOM,
    HOUSE,
    LAND,
    GARAGE
}

//добавить в PropertyMarker
enum class CurrencyType {
    USD,
    EUR,
    BYN
}
//добавить в PropertyMarker
enum class SellerType {
    OWNER,
    AGENT_BUILDER
}

//добавить в PropertyMarker
enum class SortType {
    NEWEST,
    PRICE_ASC,
    PRICE_DESC
}

//добавить в PropertyMarker
enum class DealType {
    BUY,
    RENT
}

//Добавить
enum class CommercialPropertyType {
    OFFICE,
    SHOP,
    INDUSTRIAL,
    WAREHOUSE,
    OTHER
}
//добавить в PropertyMarker
enum class CommercialAmenity {
    FINISHING,
    PRIVATE_BATHROOM,
    HOT_WATER,
    COLD_WATER,
    HEATING,
    SEPARATE_ENTRANCE
}
enum class PropertyStatus {
    FOR_RENT,
    FOR_SALE
}

data class PropertyMarker(
    val id: String,
    val lat: Double,
    val lon: Double,
    val type: PropertyType,

    // Основные характеристики
    val squareMeters: Double,               // Площадь в квадратных метрах
    val rooms: Int? = null,                 // Количество комнат (опционально, не для всех типов, напр. гараж)
    val bedrooms: Int? = null,              // Количество спален (для жилой недвижимости)
    val bathrooms: Int? = null,             // Количество ванных комнат

    // Финансовые аспекты
    val price: Double,                      // Цена (аренда/продажа)
    val currency: String = "USD",           // Валюта (по умолчанию USD, можно enum: USD, EUR, RUB etc.)
    val status: PropertyStatus,             // Статус: для аренды, продажи, продан и т.д.

    // Дополнительная информация для отображения/фильтрации
    val address: String? = null,            // Полный адрес (для попапа на карте)
    val description: String? = null,        // Краткое описание
    val photos: List<String> = emptyList(), // Список URL фото (для показа в деталях)

    // Строительные детали
    val floor: Int? = null,                 // Этаж (для квартир/офисов)
    val totalFloors: Int? = null,           // Общее количество этажей в здании
    val yearBuilt: Int? = null,             // Год постройки

    // Для фильтрации и ролей
    val ownerId: String? = null,            // ID владельца (для ролевой модели: кто может редактировать)
    val isFavorite: Boolean = false,        // Флаг "избранное" для пользователя
    val amenities: List<String> = emptyList() // Удобства: "WiFi", "Парковка", "Балкон" etc. (для фильтров)
)