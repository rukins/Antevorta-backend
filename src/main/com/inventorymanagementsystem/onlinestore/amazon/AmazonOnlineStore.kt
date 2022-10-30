package com.inventorymanagementsystem.onlinestore.amazon

import com.inventorymanagementsystem.model.OnlineStoreType
import com.inventorymanagementsystem.onlinestore.AbstractOnlineStore
import com.inventorymanagementsystem.onlinestore.AbstractOnlineStoreProduct

class AmazonOnlineStore(private var storeName: String, private var accessKey: String) : AbstractOnlineStore()  {
    override fun getById(id: Long): AbstractOnlineStoreProduct {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<AbstractOnlineStoreProduct> {
        TODO("Not yet implemented")
    }

    override fun post(entity: AbstractOnlineStoreProduct): AbstractOnlineStoreProduct {
        TODO("Not yet implemented")
    }

    override fun put(entity: AbstractOnlineStoreProduct, id: Long): AbstractOnlineStoreProduct {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long): Void {
        TODO("Not yet implemented")
    }

    override fun getType(): OnlineStoreType {
        return OnlineStoreType.AMAZON
    }
}