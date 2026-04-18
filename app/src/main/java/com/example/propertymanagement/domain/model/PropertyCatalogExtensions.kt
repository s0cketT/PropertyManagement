package com.example.propertymanagement.domain.model

/** Объявления, видимые в общем каталоге (лента, карта). */
fun List<Property>.visibleInPublicCatalog(): List<Property> =
    filter { it.moderationStatus == ModerationStatus.APPROVED }
