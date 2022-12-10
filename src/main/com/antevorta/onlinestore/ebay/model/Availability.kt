package com.antevorta.onlinestore.ebay.model

data class Availability (
    var pickupAtLocationAvailability: PickupAtLocationAvailability? = null,
    var shipToLocationAvailability: ShipToLocationAvailability? = null
)
