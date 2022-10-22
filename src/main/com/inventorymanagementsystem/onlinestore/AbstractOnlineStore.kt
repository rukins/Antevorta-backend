package com.inventorymanagementsystem.onlinestore

import com.inventorymanagementsystem.model.OnlineStoreType
import com.inventorymanagementsystem.onlinestore.amazon.AmazonOnlineStore
import com.inventorymanagementsystem.onlinestore.shopify.ShopifyOnlineStore

abstract class AbstractOnlineStore {
    abstract fun getById(id: Long): AbstractOnlineStoreProduct

    abstract fun getAll(): List<AbstractOnlineStoreProduct>

    abstract fun post(entity: AbstractOnlineStoreProduct): AbstractOnlineStoreProduct

    abstract fun put(entity: AbstractOnlineStoreProduct, id: Long): AbstractOnlineStoreProduct

    abstract fun delete(id: Long): Void

    companion object {
        @JvmStatic
        fun createOnlineStore(type: OnlineStoreType, storeName: String, accessToken: String): AbstractOnlineStore {
            return when (type) {
                OnlineStoreType.SHOPIFY -> ShopifyOnlineStore(storeName, accessToken)
                OnlineStoreType.AMAZON -> AmazonOnlineStore(storeName, accessToken)
            }
        }
    }
}