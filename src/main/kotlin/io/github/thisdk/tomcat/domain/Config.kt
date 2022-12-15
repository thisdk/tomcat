package io.github.thisdk.tomcat.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class Config(
    @JsonProperty("version")
    val version: String,
    @JsonProperty("code")
    val code: String,
    @JsonProperty("sync")
    val sync: String,
    @JsonProperty("list")
    val list: List<Account>
)
