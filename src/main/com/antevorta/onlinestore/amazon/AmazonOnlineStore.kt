package com.antevorta.onlinestore.amazon

import com.antevorta.model.OnlineStoreType
import com.antevorta.onlinestore.AbstractOnlineStore
import com.antevorta.onlinestore.AbstractOnlineStoreProduct

class AmazonOnlineStore(private var storeName: String, private var accessKey: String) : AbstractOnlineStore()  {
    override fun getById(id: String): AbstractOnlineStoreProduct {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<AbstractOnlineStoreProduct> {
        TODO("Not yet implemented")
    }

    override fun post(entity: AbstractOnlineStoreProduct): AbstractOnlineStoreProduct {
        TODO("Not yet implemented")
    }

    override fun put(entity: AbstractOnlineStoreProduct, id: String): AbstractOnlineStoreProduct {
        TODO("Not yet implemented")
    }

    override fun delete(id: String): Void {
        TODO("Not yet implemented")
    }

    override fun getType(): OnlineStoreType {
        return OnlineStoreType.AMAZON
    }
}