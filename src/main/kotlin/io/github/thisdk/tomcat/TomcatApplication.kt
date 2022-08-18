package io.github.thisdk.tomcat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class TomcatApplication

fun main(args: Array<String>) {
    runApplication<TomcatApplication>(*args)
}
