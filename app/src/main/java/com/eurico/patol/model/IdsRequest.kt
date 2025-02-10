package com.eurico.patol.model

import kotlinx.serialization.Serializable

@Serializable
data class IdsRequest(
    val ids: List<Long>
)