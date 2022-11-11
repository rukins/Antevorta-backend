package com.antevorta.onlinestore

import com.antevorta.model.OnlineStoreType
import com.antevorta.onlinestore.amazon.AmazonOnlineStore
import com.antevorta.onlinestore.shopify.ShopifyOnlineStore

abstract class AbstractOnlineStore {
    abstract fun getById(id: Long): AbstractOnlineStoreProduct

    abstract fun getAll(): List<AbstractOnlineStoreProduct>

    abstract fun post(entity: AbstractOnlineStoreProduct): AbstractOnlineStoreProduct

    abstract fun put(entity: AbstractOnlineStoreProduct, id: Long): AbstractOnlineStoreProduct

    abstract fun delete(id: Long): Void

    abstract fun getType(): OnlineStoreType

    companion object {
        @JvmStatic
        fun create(type: OnlineStoreType, storeName: String, accessToken: String): AbstractOnlineStore {
            return when (type) {
                OnlineStoreType.SHOPIFY -> ShopifyOnlineStore(storeName, accessToken)
                OnlineStoreType.AMAZON -> AmazonOnlineStore(storeName, accessToken)
            }
        }
    }
}