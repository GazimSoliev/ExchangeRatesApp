package com.gazim.app.exchange_rates.resource

import java.io.InputStream

object Resources {

    fun getInputStream(patchInResource: String): InputStream? =
        javaClass.classLoader.getResourceAsStream(patchInResource).also { println("res: $it") }

    fun <T> getAndUseInputStream(patchInResource: String, block: (InputStream?) -> T) =
        getInputStream(patchInResource).use(block)

}