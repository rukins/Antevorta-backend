package com.inventorymanagementsystem.onlinestore.shopify

import com.inventorymanagementsystem.onlinestore.shopify.model.Product
import feign.Headers
import feign.Param
import feign.RequestLine
import org.springframework.stereotype.Component

@Component
interface ShopifyClient {
    @RequestLine("GET /products/{product_id}.json")
    @Headers("X-Shopify-Access-Token: {access_token}")
    fun getById(@Param("product_id") id: Long, @Param("access_token") accessToken: String): WrappedProduct

    @RequestLine("GET /products.json")
    @Headers("X-Shopify-Access-Token: {access_token}")
    fun getAll(@Param("access_token") accessToken: String): WrappedProducts

    @RequestLine("POST /products.json")
    @Headers("Content-Type: application/json", "X-Shopify-Access-Token: {access_token}")
    fun post(entity: WrappedProduct, @Param("access_token") accessToken: String): WrappedProduct

    @RequestLine("PUT /products/{product_id}.json")
    @Headers("Content-Type: application/json", "X-Shopify-Access-Token: {access_token}")
    fun put(entity: WrappedProduct, @Param("product_id") id: Long, @Param("access_token") accessToken: String): WrappedProduct

    @RequestLine("DELETE /products/{product_id}.json")
    @Headers("X-Shopify-Access-Token: {access_token}")
    fun delete(@Param("product_id") id: Long, @Param("access_token") accessToken: String): Void
}

data class WrappedProducts(var products: List<Product>)
data class WrappedProduct(var product: Product)

fun getWrappedProducts(products: List<Product>): WrappedProducts {
    return WrappedProducts(products)
}

fun getWrappedProduct(product: Product): WrappedProduct {
    return WrappedProduct(product)
}