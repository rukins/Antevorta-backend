package com.inventorymanagementsystem.onlinestore.shopify.model

import com.inventorymanagementsystem.onlinestore.AbstractOnlineStoreProduct

data class Product (
    var id: Long? = null, var title: String? = null, var body_html: String? = null, var vendor: String? = null,
    var product_type: String? = null, var created_at: String? = null, var handle: String? = null,
    var updated_at: String? = null, var published_at: String? = null, var template_suffix: String? = null,
    var status: String? = null, var published_scope: String? = null, var tags: String? = null,
    var admin_graphql_api_id: String? = null, var variants: List<Variant>? = null, var options: List<Option>? = null,
    var images: List<Image>? = null, var image: Image? = null
) : AbstractOnlineStoreProduct()
