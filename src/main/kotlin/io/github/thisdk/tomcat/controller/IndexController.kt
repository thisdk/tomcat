package io.github.thisdk.tomcat.controller

import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {

    @ResponseBody
    @RequestMapping("/")
    @Cacheable(value = ["index"])
    fun hello(): String {
        return "Hello World -> ${System.currentTimeMillis()}"
    }

}