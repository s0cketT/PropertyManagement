package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.WallMaterialType
import com.example.propertymanagement.R

fun WallMaterialType.titleRes(): Int = when (this) {
    WallMaterialType.PANEL -> R.string.wall_panel
    WallMaterialType.MONOLITH -> R.string.wall_monolith
    WallMaterialType.BRICK -> R.string.wall_brick
}