package com.inventorymanagementsystem.onlinestore.shopify.model

import com.inventorymanagementsystem.onlinestore.AbstractOnlineStoreProduct

data class Product (
    private var id: Long? = null, private var title: String? = null, var body_html: String? = null, var vendor: String? = null,
    var product_type: String? = null, var created_at: String? = null, var handle: String? = null,
    var updated_at: String? = null, var published_at: String? = null, var template_suffix: String? = null,
    var status: String? = null, var published_scope: String? = null, var tags: String? = null,
    var admin_graphql_api_id: String? = null, var variants: List<Variant>? = null, var options: List<Option>? = null,
    var images: List<Image>? = null, var image: Image? = null
) : AbstractOnlineStoreProduct() {
    override fun getId(): Long {
        return this.id!!
    }

    fun setId(id: Long?) {
        this.id = id
    }

    override fun getTitle(): String {
        return this.title!!
    }

    fun setTitle(title: String?) {
        this.title = title
    }
}
