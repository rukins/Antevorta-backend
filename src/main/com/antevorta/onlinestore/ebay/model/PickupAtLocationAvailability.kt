package com.antevorta.onlinestore.ebay.model

data class PickupAtLocationAvailability (
    var availabilityType: String? = null, var merchantLocationKey: String? = null,
    var quantity: Long? = null, var fulfillmentTime: FulfillmentTime? = null,
)