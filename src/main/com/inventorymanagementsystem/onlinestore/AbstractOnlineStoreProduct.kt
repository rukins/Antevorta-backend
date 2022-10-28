package com.inventorymanagementsystem.onlinestore

import com.inventorymanagementsystem.model.OnlineStoreType
import com.inventorymanagementsystem.onlinestore.amazon.AmazonOnlineStore
import com.inventorymanagementsystem.onlinestore.shopify.ShopifyOnlineStore
import com.inventorymanagementsystem.onlinestore.shopify.model.Product
import kotlin.reflect.KClass

abstract class AbstractOnlineStoreProduct {
    abstract fun getId(): Long
    abstract fun getTitle(): String

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