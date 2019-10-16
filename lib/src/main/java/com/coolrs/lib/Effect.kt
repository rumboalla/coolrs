package com.coolrs.lib

interface Effect {
    fun start()
    fun stop()
    fun name(): String
}