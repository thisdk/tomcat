package io.github.thisdk.tomcat.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {

    @ResponseBody
    @RequestMapping("/")
    fun root(): String {
        return "Hello World"
    }

}