package com.inventorymanagementsystem.onlinestore.shopify

import com.inventorymanagementsystem.model.OnlineStoreType
import com.inventorymanagementsystem.onlinestore.AbstractOnlineStore
import com.inventorymanagementsystem.onlinestore.AbstractOnlineStoreProduct
import com.inventorymanagementsystem.onlinestore.shopify.model.Product
import feign.Feign
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import feign.httpclient.ApacheHttpClient

class ShopifyOnlineStore (private var storeName: String, private var accessKey: String) : AbstractOnlineStore() {
    private val client: ShopifyClient = Feign.builder()
        .client(ApacheHttpClient())
        .encoder(GsonEncoder())
        .decoder(GsonDecoder())
        .target(ShopifyClient::class.java, "https://${storeName}.myshopify.com/admin/api/2022-10")

    override fun getById(id: Long): AbstractOnlineStoreProduct {
        return client.getById(id, accessKey).product
    }

    override fun getAll(): List<AbstractOnlineStoreProduct> {
        return client.getAll(accessKey).products
    }

    override fun post(entity: AbstractOnlineStoreProduct): AbstractOnlineStoreProduct {
        return client.post(getWrappedProduct(entity as Product), accessKey).product
    }

    override fun put(entity: AbstractOnlineStoreProduct, id: Long): AbstractOnlineStoreProduct {
        return client.put(getWrappedProduct(entity as Product),id, accessKey).product
    }

    override fun delete(id: Long): Void {
        return client.delete(id, accessKey)
    }

    override fun getType(): OnlineStoreType {
        return OnlineStoreType.SHOPIFY
    }
}