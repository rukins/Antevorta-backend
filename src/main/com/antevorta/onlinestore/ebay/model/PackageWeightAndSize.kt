package com.antevorta.onlinestore.ebay.model

data class PackageWeightAndSize (
    var packageType: String? = null, var dimensions: Dimensions? = null, var weight: Weight? = null
)