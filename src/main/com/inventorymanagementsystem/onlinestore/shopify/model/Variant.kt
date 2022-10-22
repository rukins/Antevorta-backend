package com.inventorymanagementsystem.onlinestore.shopify.model

data class Variant(
    var id: Long? = null, var product_id: Long? = null, var title: String? = null, var price: String? = null,
    var sku: String? = null, var position: Long? = null, var inventory_policy: String? = null,
    var compare_at_price: String? = null, var fulfillment_service: String? = null, var inventory_management: String? = null,
    var option1: String? = null, var option2: String? = null, var option3: String? = null, var created_at: String? = null,
    var updated_at: String? = null, var taxable: Boolean? = null, var barcode: String? = null, var grams: Long? = null,
    var image_id: Long? = null, var weight: Double? = null, var weight_unit: String? = null, var inventory_item_id: Long? = null,
    var inventory_quantity: Long? = null, var old_inventory_quantity: Long? = null, var requires_shipping: Boolean? = null,
    var admin_graphql_api_id: String? = null
)