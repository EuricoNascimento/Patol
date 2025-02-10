package com.eurico.patol.model

enum class ContentType {
    TEXT,
    IMAGE_TEXT;

    companion object{
        fun getContentType(order: Int): ContentType {
            return values().firstOrNull { it.ordinal == order } ?: TEXT
        }
    }
}