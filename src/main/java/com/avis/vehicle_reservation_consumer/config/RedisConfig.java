package com.avis.vehicle_reservation_consumer.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("redis-10270.crce199.us-west-2-2.ec2.redns.redis-cloud.com");
        config.setPort(10270);
        config.setPassword(RedisPassword.of("jIqqoBtWankjEVNzc811dKGxcpckCWgr"));
        return new LettuceConnectionFactory(config);
    }
}

