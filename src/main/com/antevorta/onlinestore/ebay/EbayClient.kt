package com.antevorta.onlinestore.ebay

import com.antevorta.onlinestore.ebay.model.InventoryItem
import com.fasterxml.jackson.annotation.JsonProperty
import feign.Body
import feign.Headers
import feign.Param
import feign.RequestLine

interface EbayClient {
    @RequestLine("GET /sell/inventory/v1/inventory_item/{SKU}")
    @Headers("Content-Type: application/json", "Accept: application/json", "Authorization: Bearer {access_token}")
    fun getBySku(@Param("SKU") sku: String, @Param("access_token") accessToken: String): InventoryItem

    @RequestLine("GET /sell/inventory/v1/inventory_item")
    @Headers("Content-Type: application/json", "Accept: application/json", "Authorization: Bearer {access_token}")
    fun getAll(@Param("access_token") accessToken: String): WrappedInventoryItems

    @RequestLine("PUT /sell/inventory/v1/inventory_item/{SKU}")
    @Headers(
        "Content-Type: application/json", "Accept: application/json",
        "Content-Language: en-US", "Authorization: Bearer {access_token}"
    )
    fun postOrPut(entity: InventoryItem, @Param("SKU") sku: String, @Param("access_token") accessToken: String): Void

    @RequestLine("DELETE /sell/inventory/v1/inventory_item/{SKU}")
    @Headers("Content-Type: application/json", "Accept: application/json", "Authorization: Bearer {access_token}")
    fun delete(@Param("SKU") sku: String, @Param("access_token") accessToken: String): Void

    @RequestLine("POST /identity/v1/oauth2/token")
    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "Accept: application/json",
        "Authorization: Basic {encoded_credentials}"
    )
    @Body("grant_type=refresh_token&refresh_token={refresh_token}")
    fun getAccessToken(
        @Param("encoded_credentials") encodedCredentials: String, @Param("refresh_token") refreshToken: String
    ): EbayAccessToken
}

data class WrappedInventoryItems(@JsonProperty("inventoryItems") var inventoryItems: List<InventoryItem>)

fun getWrappedInventoryItems(inventoryItems: List<InventoryItem>): WrappedInventoryItems {
    return WrappedInventoryItems(inventoryItems)
}

data class EbayAccessToken(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("expires_in") val expiresIn: Long,
    @JsonProperty("token_type") val tokenType: String
)