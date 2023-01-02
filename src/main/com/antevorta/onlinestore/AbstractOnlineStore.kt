package com.antevorta.onlinestore

import com.antevorta.exception.serverexception.NotImplementedException
import com.antevorta.model.OnlineStoreCredentials
import com.antevorta.model.OnlineStoreType
import com.antevorta.onlinestore.ebay.EbayOnlineStore
import com.antevorta.onlinestore.shopify.ShopifyOnlineStore

abstract class AbstractOnlineStore {
    open fun getById(id: String): AbstractOnlineStoreProduct {
        throw NotImplementedException("Not implemented yet")
    }

    open fun getAll(): List<AbstractOnlineStoreProduct> {
        throw NotImplementedException("Not implemented yet")
    }

    open fun post(entity: AbstractOnlineStoreProduct): AbstractOnlineStoreProduct {
        throw NotImplementedException("Not implemented yet")
    }

    open fun put(entity: AbstractOnlineStoreProduct, id: String): AbstractOnlineStoreProduct {
        throw NotImplementedException("Not implemented yet")
    }

    open fun delete(id: String): Void {
        throw NotImplementedException("Not implemented yet")
    }

    abstract fun getType(): OnlineStoreType

    companion object {
        @JvmStatic
        fun create(type: OnlineStoreType, credentials: OnlineStoreCredentials, arbitraryStoreName: String): AbstractOnlineStore {
            return when (type) {
                OnlineStoreType.SHOPIFY -> ShopifyOnlineStore(credentials)
                OnlineStoreType.EBAY -> EbayOnlineStore(credentials, arbitraryStoreName)
            }
        }
    }
}