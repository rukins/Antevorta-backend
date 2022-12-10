package com.antevorta.onlinestore.ebay.model

data class ShipToLocationAvailability (
    var allocationByFormat: AllocationByFormat? = null, var availabilityDistributions: AvailabilityDistributions? = null,
    var quantity: Long? = null
)