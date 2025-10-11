package com.readingjournal.app.utils

import java.util.UUID

object IdGenerator {
    fun generateId(): String = UUID.randomUUID().toString()
}
