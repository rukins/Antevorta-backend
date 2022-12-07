package com.antevorta.onlinestore

import com.antevorta.model.OnlineStoreType
import com.antevorta.onlinestore.shopify.model.Product

abstract class AbstractOnlineStoreProduct {
    abstract fun getId(): String
    abstract fun setId(id: String)
    abstract fun getTitle(): String
    abstract fun setTitle(title: String)

    companion object {
        @JvmStatic
        fun getClassByType(type: OnlineStoreType): Class<out AbstractOnlineStoreProduct> {
            return when (type) {
                OnlineStoreType.SHOPIFY -> Product::class.java
                OnlineStoreType.AMAZON -> TODO("not implemented")
            }
        }
    }
}