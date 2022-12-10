package com.antevorta.onlinestore.ebay.model

data class AvailabilityDistributions (
    var fulfillmentTime: FulfillmentTime? = null, var merchantLocationKey: String? = null,
    var quantity: Long? = null
)