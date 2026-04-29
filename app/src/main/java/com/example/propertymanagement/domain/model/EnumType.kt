package com.example.propertymanagement.domain.model

enum class PropertyType {
    COMMERCIAL,
    APARTMENT,
    ROOM,
    HOUSE,
    GARAGE
}

enum class CurrencyType {
    USD,
    EUR,
    BYN
}

enum class SellerType {
    OWNER,
    AGENT
}

enum class SortType {
    NEWEST,
    PRICE_ASC,
    PRICE_DESC
}

enum class ThemeType {
    SYSTEM, DARK, LIGHT
}

enum class LanguageType {
    RU, EN
}

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

enum class CommercialAmenity {
    FINISHING,
    PRIVATE_BATHROOM,
    HOT_WATER,
    COLD_WATER,
    HEATING,
    SEPARATE_ENTRANCE
}

enum class BuildingAmenity {
    ELEVATOR,
    TRASH_CHUTE,
    GATED_AREA,
    INTERCOM,
    BASEMENT
}

enum class CommercialRepairType {
    OFFICE_FINISH,
    REQUIRES_CAPITAL_REPAIR,
    REQUIRES_COSMETIC_REPAIR
}
enum class PropertyStatus {
    FOR_RENT,
    FOR_SALE
}

/** Статус модерации объявления (имена как в `moderation_statuses.name` в БД). */
enum class ModerationStatus(val dbNameRu: String) {
    PENDING("На модерации"),
    REJECTED("Отклонено"),
    APPROVED("Одобрено"),
    UNKNOWN("");

    companion object {
        fun fromDb(name: String?): ModerationStatus {
            if (name.isNullOrBlank()) {
                return UNKNOWN
            }
            val trimmed = name.trim()
            entries.firstOrNull { it.dbNameRu.isNotEmpty() && it.dbNameRu == trimmed }?.let {
                return it
            }
            runCatching {
                java.lang.Enum.valueOf(ModerationStatus::class.java, trimmed.uppercase())
            }.getOrNull()?.let { return it }
            return UNKNOWN
        }
    }
}

/** Вкладки фильтра на экране «Мои объявления». */
enum class MyAdsListingFilter {
    PUBLISHED,
    PENDING,
    REJECTED,
}

enum class RoomsType {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    FIVE_PLUS
}

enum class BalconyType {
    NONE,
    BALCONY,
    LOGGIA,
    TWO_PLUS
}

enum class BathroomType {
    SEPARATE,
    COMBINED,
    TWO,
    THREE_PLUS
}

enum class ApartmentRepairType {
    COSMETIC,
    EURO,
    DESIGNER,
    ROUGH_FINISH,
    NEEDS_REPAIR,
    EMERGENCY
}

enum class CeilingHeightType {
    H_2_5,
    H_2_7,
    H_3_0,
    H_3_5,
    H_4_0
}

enum class WallMaterialType {
    PANEL,
    MONOLITH,
    BRICK
}

enum class WindowViewType {
    RIVER,
    COURTYARD,
    PARK,
    STREET,
    SOUTH,
    NORTH,
    EAST,
    WEST
}

enum class RoofType {
    FLAT,
    SINGLE_SLOPE,
    DOUBLE_SLOPE,
    OTHER
}

enum class HeatingType {
    GAS,
    CENTRAL,
    ELECTRIC,
    STOVE,
    NONE
}

enum class WaterType {
    WELL,
    WELL_SHAFT,
    CENTRAL,
    NONE
}

enum class GasType {
    IN_HOUSE,
    NEARBY,
    NONE
}
enum class HouseAmenity {
    FIREPLACE,
    FURNITURE,
    JACUZZI,
    PARKING,
    GARAGE,
    SECURITY
}
enum class HouseType {
    HOUSE,
    COTTAGE,
    DACHA,
    OTHER
}
enum class ParkingType {
    ROOF,
    UNDERGROUND,
    GROUND,
    OPEN
}