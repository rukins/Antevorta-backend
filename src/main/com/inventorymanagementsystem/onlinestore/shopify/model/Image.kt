package com.inventorymanagementsystem.onlinestore.shopify.model

data class Image (
    var id: Long? = null, var product_id: Long? = null, var position: Long? = null, var created_at: String? = null,
    var updated_at: String? = null, var alt: String? = null, var width: Long? = null, var height: Long? = null,
    var src: String? = null, var variant_ids: List<Long>? = null, var admin_graphql_api_id: String? = null
)