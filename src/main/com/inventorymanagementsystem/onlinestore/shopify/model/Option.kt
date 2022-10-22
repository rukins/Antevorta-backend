package com.inventorymanagementsystem.onlinestore.shopify.model

data class Option (
    var id: Long? = null, var product_id: Long? = null, var name: String? = null, var position: String? = null,
    var values: List<String>? = null
)