package com.antevorta.onlinestore.ebay

import com.antevorta.model.onlinestorecredentials.OnlineStoreCredentials
import com.antevorta.model.OnlineStoreType
import com.antevorta.onlinestore.AbstractOnlineStore
import com.antevorta.onlinestore.AbstractOnlineStoreProduct
import com.antevorta.onlinestore.ebay.model.InventoryItem
import feign.Feign
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import feign.httpclient.ApacheHttpClient
import java.util.Base64
import java.util.UUID

class EbayOnlineStore(private val credentials: OnlineStoreCredentials) : AbstractOnlineStore() {
    private val client: EbayClient = Feign.builder()
        .client(ApacheHttpClient())
        .encoder(GsonEncoder())
        .decoder(GsonDecoder())
        .target(
            EbayClient::class.java,
            "https://" + (if (credentials.storeName != null) credentials.storeName else "api") + ".ebay.com"
        )

    override fun getById(id: String): AbstractOnlineStoreProduct {
        return client.getBySku(id, getAccessToken())
    }

    override fun getAll(): List<AbstractOnlineStoreProduct> {
        return client.getAll(getAccessToken()).inventoryItems
    }

    override fun post(entity: AbstractOnlineStoreProduct): AbstractOnlineStoreProduct {
        val id: String = UUID.randomUUID().toString()

        entity.setId(id)

        client.postOrPut(entity as InventoryItem, id, getAccessToken())

        return getById(id)
    }

    override fun put(entity: AbstractOnlineStoreProduct, id: String): AbstractOnlineStoreProduct {
        client.postOrPut(entity as InventoryItem, id, getAccessToken())

        return getById(id)
    }

    override fun delete(id: String): Void {
        return client.delete(id, getAccessToken())
    }

    override fun getType(): OnlineStoreType {
        return OnlineStoreType.EBAY
    }

    private fun getAccessToken(): String {
        val encodedCredentials: String = Base64.getEncoder().encodeToString(
            (credentials.extra.userName + ":" + credentials.extra.password).toByteArray()
        )

        return client.getAccessToken(encodedCredentials, credentials.token).access_token
    }
}