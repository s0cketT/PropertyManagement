package com.example.propertymanagement.domain.model


fun Property.isRejectedOrPendingForCatalog(): Boolean {
    if (moderationStatus == ModerationStatus.REJECTED ||
        moderationStatus == ModerationStatus.PENDING
    ) {
        return true
    }
    return moderationStatusId == ModerationStatus.REJECTED_STATUS_ID ||
        moderationStatusId == ModerationStatus.PENDING_STATUS_ID
}

fun Property.isApprovedForPublicCatalog(): Boolean = !isRejectedOrPendingForCatalog()

fun List<Property>.visibleInPublicCatalog(): List<Property> =
    filter { it.isApprovedForPublicCatalog() }

/** Каталог (список, карта, фильтры): без отклонённых и «на модерации». */
fun List<Property>.forMainCatalogDisplay(): List<Property> = visibleInPublicCatalog()
