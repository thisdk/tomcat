package io.github.thisdk.tomcat.redis

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
class RedisConfig : CachingConfigurerSupport() {

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        val serializer: RedisSerializer<Any> = redisSerializer()
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = serializer
        redisTemplate.hashKeySerializer = StringRedisSerializer()
        redisTemplate.hashValueSerializer = serializer
        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }

    @Bean
    fun redisSerializer(): RedisSerializer<Any> {
        val serializer = Jackson2JsonRedisSerializer(Any::class.java)
        val objectMapper = ObjectMapper()
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        objectMapper.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.WRAPPER_ARRAY
        )
        serializer.setObjectMapper(objectMapper)
        return serializer
    }

    @Bean
    fun redisCacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {
        val redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory)
        val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer()))
            .entryTtl(Duration.ofHours(1))
        return RedisCacheManager(redisCacheWriter, redisCacheConfiguration)
    }

}