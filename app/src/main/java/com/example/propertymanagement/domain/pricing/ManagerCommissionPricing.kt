package com.example.propertymanagement.domain.pricing

object ManagerCommissionPricing {


    fun grossListingAmount(listedPrice: Double, commissionPercent: Double): Double {
        if (commissionPercent <= 0.0 || listedPrice <= 0.0) {
            return listedPrice
        }
        val fee = commissionAmount(listedPrice, commissionPercent)
        return listedPrice + fee
    }

    fun commissionAmount(listedPrice: Double, commissionPercent: Double): Double {
        if (commissionPercent <= 0.0 || listedPrice <= 0.0) {
            return 0.0
        }
        return listedPrice * (commissionPercent / 100.0)
    }
}
