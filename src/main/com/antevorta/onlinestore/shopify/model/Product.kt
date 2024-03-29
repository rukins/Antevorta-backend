package com.antevorta.onlinestore.shopify.model

import com.antevorta.onlinestore.AbstractOnlineStoreProduct

data class Product (
    private var id: Long? = null, private var title: String? = null, var body_html: String? = null, var vendor: String? = null,
    var product_type: String? = null, var created_at: String? = null, var handle: String? = null,
    var updated_at: String? = null, var published_at: String? = null, var template_suffix: String? = null,
    var status: String? = null, var published_scope: String? = null, var tags: String? = null,
    var admin_graphql_api_id: String? = null, var variants: List<Variant>? = null, var options: List<Option>? = null,
    var images: List<Image>? = null, var image: Image? = null
) : AbstractOnlineStoreProduct() {
    override fun getId(): String {
        return this.id!!.toString()
    }

    override fun setId(id: String) {
        this.id = id.toLong()
    }

    override fun getTitle(): String {
        return this.title!!
    }

    override fun setTitle(title: String) {
        this.title = title
    }
}
