package com.antevorta.onlinestore.ebay

import com.antevorta.context.SpringContext
import com.antevorta.model.OnlineStoreCredentials
import com.antevorta.model.OnlineStoreType
import com.antevorta.onlinestore.AbstractOnlineStore
import com.antevorta.onlinestore.AbstractOnlineStoreProduct
import com.antevorta.onlinestore.ebay.model.InventoryItem
import com.antevorta.repository.redis.AccessTokenRepository
import feign.Feign
import feign.httpclient.ApacheHttpClient
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import java.util.*

class EbayOnlineStore(private val credentials: OnlineStoreCredentials) : AbstractOnlineStore() {
    private val client: EbayClient = Feign.builder()
        .client(ApacheHttpClient())
        .encoder(JacksonEncoder())
        .decoder(JacksonDecoder())
        .target(
            EbayClient::class.java,
            "https://" + (if (credentials.storeName != null) credentials.storeName else "api") + ".ebay.com"
        )

    private val accessTokenRepository: AccessTokenRepository = SpringContext.getBean(AccessTokenRepository::class.java)
    private val accessTokenKeyPrefix: String = SpringContext.getProperty("redis.cache.key-prefix.ebay")

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
        val accessToken: String? = accessTokenRepository.getByKeyPrefix(accessTokenKeyPrefix)

        if (accessToken != null)
            return accessToken

        val encodedCredentials: String = Base64.getEncoder().encodeToString(
            (credentials.extra.username + ":" + credentials.extra.password).toByteArray()
        )

        val ebayAccessToken: EbayAccessToken = client.getAccessToken(encodedCredentials, credentials.token)

        accessTokenRepository.save(accessTokenKeyPrefix, ebayAccessToken.accessToken, ebayAccessToken.expiresIn)

        return ebayAccessToken.accessToken
    }
}