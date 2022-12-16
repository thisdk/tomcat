package io.github.thisdk.tomcat.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.thisdk.tomcat.domain.Config
import io.github.thisdk.tomcat.redis.RedisService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.util.ResourceUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@RestController
class IndexController {

    companion object {
        const val TIME_OUT: Long = 60 * 5
    }

    val objectMapping: ObjectMapper by lazy { ObjectMapper() }

    val config: File by lazy { ResourceUtils.getFile("classpath:config.json") }

    @Autowired
    lateinit var redis: RedisService

    @ResponseBody
    @RequestMapping("/")
    @Cacheable(value = ["jason"], key = "'index'")
    fun hello(): String {
        return "Hello World -> ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())}"
    }

    @ResponseBody
    @RequestMapping("/sync")
    @Cacheable(value = ["jason"], key = "'sync:' + #code")
    fun sync(code: String?): String {
        val json = config.bufferedReader().use { it.readText() }
        val configObject = objectMapping.readValue(json, Config::class.java)
        if (code == configObject.sync) {
            redis["jason::version", configObject.version] = TIME_OUT
            configObject.list.forEach {
                redis[
                        "jason::password:${configObject.code}:${it.name}",
                        "username:</br></br>${it.user}</br></br>password:</br></br>${it.value}"
                ] = TIME_OUT
            }
            return "Sync ${configObject.version} Success -> ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())}"
        }
        return "Sync 1.0.0 Success -> ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())}"
    }

    @ResponseBody
    @RequestMapping("/version")
    @Cacheable(value = ["jason"], key = "'query:version'")
    fun version(): String {
        val version = if (redis["jason::version"] == null) "1.0.0" else redis["jason::version"] as String
        return "$version -> ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())}"
    }

    @ResponseBody
    @RequestMapping("/password")
    @Cacheable(value = ["jason"], key = "'query:' + #code + ':' + #type")
    @Synchronized
    fun password(code: String?, type: String?): String {
        Thread.sleep(1000)
        val body = redis["jason::password:${code}:${type}"]
        return if (body != null) {
            body as String
        } else {
            val username = UUID.randomUUID().toString().replace("-", "").substring(0, (6..14).random())
            val password = UUID.randomUUID().toString().replace("-", "").substring(0, (16..24).random())
            "username:</br></br>${username}</br></br>password:</br></br>${password}"
        }
    }


}