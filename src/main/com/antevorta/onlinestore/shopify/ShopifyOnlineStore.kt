package com.antevorta.onlinestore.shopify

import com.antevorta.model.OnlineStoreCredentials
import com.antevorta.model.OnlineStoreType
import com.antevorta.onlinestore.AbstractOnlineStore
import com.antevorta.onlinestore.AbstractOnlineStoreProduct
import com.antevorta.onlinestore.shopify.model.Product
import feign.Feign
import feign.httpclient.ApacheHttpClient
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder

class ShopifyOnlineStore(private val credentials: OnlineStoreCredentials) : AbstractOnlineStore() {
    private val client: ShopifyClient = Feign.builder()
        .client(ApacheHttpClient())
        .encoder(JacksonEncoder())
        .decoder(JacksonDecoder())
        .target(ShopifyClient::class.java, "https://${credentials.storeName}.myshopify.com/admin/api/2022-10")

    override fun getById(id: String): AbstractOnlineStoreProduct {
        return client.getById(id, credentials.token).product
    }

    override fun getAll(): List<AbstractOnlineStoreProduct> {
        return client.getAll(credentials.token).products
    }

    override fun post(entity: AbstractOnlineStoreProduct): AbstractOnlineStoreProduct {
        return client.post(getWrappedProduct(entity as Product), credentials.token).product
    }

    override fun put(entity: AbstractOnlineStoreProduct, id: String): AbstractOnlineStoreProduct {
        return client.put(getWrappedProduct(entity as Product), id, credentials.token).product
    }

    override fun delete(id: String): Void {
        return client.delete(id, credentials.token)
    }

    override fun getType(): OnlineStoreType {
        return OnlineStoreType.SHOPIFY
    }
}