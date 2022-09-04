package io.github.thisdk.tomcat.controller

import io.github.thisdk.tomcat.redis.RedisService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {

    @Autowired
    private lateinit var redis: RedisService

    @ResponseBody
    @RequestMapping("/")
    @Cacheable(value = ["index"])
    fun hello(): String {
        return "Hello World"
    }

}