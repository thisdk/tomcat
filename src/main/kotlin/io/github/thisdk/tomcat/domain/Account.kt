package io.github.thisdk.tomcat.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class Account(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("user")
    val user: String,
    @JsonProperty("value")
    val value: String
)
