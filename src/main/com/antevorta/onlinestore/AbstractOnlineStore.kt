package com.antevorta.onlinestore

import com.antevorta.model.onlinestorecredentials.OnlineStoreCredentials
import com.antevorta.model.OnlineStoreType
import com.antevorta.onlinestore.shopify.ShopifyOnlineStore

abstract class AbstractOnlineStore {
    abstract fun getById(id: String): AbstractOnlineStoreProduct

    abstract fun getAll(): List<AbstractOnlineStoreProduct>

    abstract fun post(entity: AbstractOnlineStoreProduct): AbstractOnlineStoreProduct

    abstract fun put(entity: AbstractOnlineStoreProduct, id: String): AbstractOnlineStoreProduct

    abstract fun delete(id: String): Void

    abstract fun getType(): OnlineStoreType

    companion object {
        @JvmStatic
        fun create(type: OnlineStoreType, credentials: OnlineStoreCredentials): AbstractOnlineStore {
            return when (type) {
                OnlineStoreType.SHOPIFY -> ShopifyOnlineStore(credentials)
                OnlineStoreType.EBAY -> TODO("not implemented")
            }
        }
    }
}