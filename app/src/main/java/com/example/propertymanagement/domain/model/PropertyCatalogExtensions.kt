package com.example.propertymanagement.domain.model


fun List<Property>.visibleInPublicCatalog(): List<Property> =
    filter { it.moderationStatus == ModerationStatus.APPROVED }


fun List<Property>.forMainCatalogDisplay(): List<Property> {
    if (isEmpty()) {
        return this
    }
    val approvedOnly = filter { it.moderationStatus == ModerationStatus.APPROVED }
    if (approvedOnly.isNotEmpty()) {
        return approvedOnly
    }
    if (all { it.moderationStatus == ModerationStatus.UNKNOWN }) {
        return map { property ->
            property.copy(moderationStatus = ModerationStatus.APPROVED)
        }
    }
    return emptyList()
}
