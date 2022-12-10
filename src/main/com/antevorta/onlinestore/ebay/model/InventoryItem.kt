package com.antevorta.onlinestore.ebay.model

import com.antevorta.onlinestore.AbstractOnlineStoreProduct

data class InventoryItem(
    var sku: String? = null, var locale: String? = null, var product: Product? = null,
    var condition: String? = null, var conditionDescription: String? = null,
    var groupIds: List<String>? = null, var inventoryItemGroupKeys: List<String>? = null,
    var availability: Availability? = null, var packageWeightAndSize: PackageWeightAndSize? = null
) : AbstractOnlineStoreProduct() {
    override fun getId(): String {
        return this.sku!!
    }

    override fun setId(id: String) {
        this.sku = id
    }

    override fun getTitle(): String {
        return product!!.title!!
    }

    override fun setTitle(title: String) {
        this.product!!.title = title
    }
}