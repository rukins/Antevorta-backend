package com.antevorta.onlinestore.shopify

import com.antevorta.onlinestore.shopify.model.Product
import com.fasterxml.jackson.annotation.JsonProperty
import feign.Headers
import feign.Param
import feign.RequestLine

interface ShopifyClient {
    @RequestLine("GET /products/{product_id}.json")
    @Headers("X-Shopify-Access-Token: {access_token}")
    fun getById(@Param("product_id") id: String, @Param("access_token") accessToken: String): WrappedProduct

    @RequestLine("GET /products.json")
    @Headers("X-Shopify-Access-Token: {access_token}")
    fun getAll(@Param("access_token") accessToken: String): WrappedProducts

    @RequestLine("POST /products.json")
    @Headers("Content-Type: application/json", "X-Shopify-Access-Token: {access_token}")
    fun post(entity: WrappedProduct, @Param("access_token") accessToken: String): WrappedProduct

    @RequestLine("PUT /products/{product_id}.json")
    @Headers("Content-Type: application/json", "X-Shopify-Access-Token: {access_token}")
    fun put(entity: WrappedProduct, @Param("product_id") id: String, @Param("access_token") accessToken: String): WrappedProduct

    @RequestLine("DELETE /products/{product_id}.json")
    @Headers("X-Shopify-Access-Token: {access_token}")
    fun delete(@Param("product_id") id: String, @Param("access_token") accessToken: String): Void
}

data class WrappedProducts(@JsonProperty("products") var products: List<Product>)
data class WrappedProduct(@JsonProperty("product") var product: Product)

fun getWrappedProducts(products: List<Product>): WrappedProducts {
    return WrappedProducts(products)
}

fun getWrappedProduct(product: Product): WrappedProduct {
    return WrappedProduct(product)
}