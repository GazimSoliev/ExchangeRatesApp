package com.gazim.app.exchange_rates.ui.model

import kotlin.reflect.KClass

interface Arguments {
    fun <T : Any> putObject(key: String, value: T)
    fun <T : Any> getObject(key: String, clazz: KClass<T>): T?
}