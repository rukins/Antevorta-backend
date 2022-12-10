package com.antevorta.onlinestore.ebay.model

data class Product (
    var title: String? = null, var description: String? = null, var upc: List<String>? = null,
    var imageUrls: List<String>? = null, var aspects: String? = null,
    var brand: String? = null, var ean: List<String>? = null, var epid: String? = null,
    var isbn: List<String>? = null, var mpn: String? = null, var videoIds: List<String>? = null,
)